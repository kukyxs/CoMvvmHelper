package com.kk.android.comvvmhelper.utils.download

import android.content.Context
import android.webkit.MimeTypeMap
import com.kk.android.comvvmhelper.anno.PublicDirectoryType
import com.kk.android.comvvmhelper.helper.SingletonHelperArg1
import com.kk.android.comvvmhelper.helper.copyFileToPublic
import com.kk.android.comvvmhelper.helper.iPrint
import com.kk.android.comvvmhelper.helper.kLogger
import com.kk.android.comvvmhelper.utils.download.downloader.NormalDownloader
import com.kk.android.comvvmhelper.utils.download.downloader.RangeDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit

data class DownloaderWrapper(
    var url: String = "",
    // 是否下载到公共存储目录，如果为 true，需要配置 [publicStoreFileName, relativePath, targetPublic]
    // 如果为 false，则配置 [privateStoreFile] 即可
    var downloadToPublic: Boolean = true,
    // 私有目录存储配置
    var privateStoreFile: File? = null,
    // 公共目录存储配置
    var publicStoreFileName: String = "",
    var publicPrimaryDir: String = "",
    var targetPublicDir: PublicDirectoryType = PublicDirectoryType.DOWNLOADS,
    // 如果当前文件存在是否覆盖
    var downloadIfFileExists: Boolean = false,
    var onDownloadProgressChange: (suspend (Float) -> Unit)? = null,
    var onDownloadFailed: (suspend (Throwable) -> Unit)? = null,
    var onDownloadCompleted: (suspend (File?) -> Unit)? = null
)

class Downloader(private val context: Context) {
    companion object : SingletonHelperArg1<Downloader, Context>(::Downloader)

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60_000, TimeUnit.MILLISECONDS)
            .readTimeout(60_000, TimeUnit.MILLISECONDS)
            .build()
    }

    private val downloadPools = hashMapOf<String, Job>()

    private val downloadSp by lazy {
        context.getSharedPreferences("kdownload_range_share", Context.MODE_PRIVATE)
    }

    fun stop(url: String) {
        if (downloadPools.contains(url)) downloadPools[url]?.cancel()
    }

    fun stopAll() = downloadPools.forEach { it.value.cancel() }

    fun allDownloadingUrls() = downloadPools.keys

    suspend fun download(wrapper: DownloaderWrapper.() -> Unit) {
        val configs = DownloaderWrapper().apply(wrapper)
        val urlSupportRange = downloadSp.contains(configs.url)

        var file: File? = null
        if (urlSupportRange) downloadSp.getString(configs.url, "")?.let { file = File(it) }

        val request = Request.Builder()
            .header("Range", "bytes=${file?.tmpFile()?.length() ?: 0}-")
            .url(configs.url).build()

        withContext(Dispatchers.IO) {
            val response = okHttpClient.newCall(request).execute()
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(response.contentType())

            if (file == null) {
                val fileName = response.fileName().ifEmpty {
                    configs.onDownloadFailed?.invoke(DownloadException("create file failed"))
                    return@withContext
                }

                file = File(context.filesDir, "cov_download/${fileName}")
            }

            val storeFile = file!!
            if (storeFile.tmpFile().inUse()) {
                kLogger.iPrint { "file is used by other process" }
                return@withContext
            }

            val downloader = if (urlSupportRange || response.supportRanges()) RangeDownloader().apply {
                if (!downloadSp.contains(configs.url)) {
                    downloadSp.edit().putString(configs.url, storeFile.canonicalPath).apply()
                }
            } else NormalDownloader()

            downloader.apply {
                onDownloadFailed = {
                    withContext(Dispatchers.Main) {
                        configs.onDownloadFailed?.invoke(it)
                    }
                }
                onProgressChange = {
                    withContext(Dispatchers.Main) {
                        configs.onDownloadProgressChange?.invoke(it)
                    }
                }
                onDownloadCompleted = {
                    var finalFile: File? = storeFile
                    if (configs.downloadToPublic) {
                        val publicFile = getPublicFile(configs.publicStoreFileName, configs.publicPrimaryDir, configs.targetPublicDir)
                        if (!publicFile.exists()) {
                            finalFile = context.copyFileToPublic(storeFile, configs.publicStoreFileName, configs.publicPrimaryDir, mimeType, configs.targetPublicDir)
                            storeFile.delete()
                        } else if (configs.downloadIfFileExists) {
                            finalFile = context.copyFileToPublic(storeFile, configs.publicStoreFileName, configs.publicPrimaryDir, mimeType, configs.targetPublicDir)
                            storeFile.delete()
                        } else {
                            finalFile = publicFile
                        }
                        downloadCompleted(configs, finalFile)
                    } else if (configs.privateStoreFile != null) {
                        try {
                            finalFile = storeFile.copyTo(configs.privateStoreFile!!, configs.downloadIfFileExists)
                            storeFile.delete()
                            downloadCompleted(configs, finalFile)
                        } catch (e: Exception) {
                            configs.onDownloadFailed?.invoke(e)
                        }
                    } else {
                        downloadCompleted(configs, finalFile)
                    }
                }
            }.download(response, storeFile)?.let { job ->
                downloadPools[configs.url] = job
            }
        }
    }

    private suspend fun downloadCompleted(configs: DownloaderWrapper, finalFile: File?) {
        downloadPools.remove(configs.url)
        if (downloadSp.contains(configs.url)) downloadSp.edit().remove(configs.url).apply()
        withContext(Dispatchers.Main) {
            configs.onDownloadCompleted?.invoke(finalFile)
        }
    }
}