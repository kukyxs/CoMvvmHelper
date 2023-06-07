package com.kk.android.comvvmhelper.utils.download

import android.content.Context
import com.kk.android.comvvmhelper.anno.PublicDirectoryType
import com.kk.android.comvvmhelper.helper.SingletonHelperArg1
import com.kk.android.comvvmhelper.helper.copyFileToPublic
import com.kk.android.comvvmhelper.utils.download.downloader.NormalDownloader
import com.kk.android.comvvmhelper.utils.download.downloader.RangeDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Source
import okio.buffer
import java.io.File
import java.util.concurrent.TimeUnit

data class DownloaderWrapper(
    var url: String = "",
    var headers: HashMap<String, String> = hashMapOf(),
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
    var onDownloadProgressChange: (suspend (progress: Float) -> Unit)? = null,
    var onDownloadFailed: (suspend (thr: Throwable) -> Unit)? = null,
    var onDownloadCompleted: (suspend (storeFile: File?) -> Unit)? = null,
    var onDownloadSpeed: ((bytes: Long, elapsedTime: Long, contentLength: Long) -> Unit)? = null
)

internal fun interface ProgressListener {
    fun update(byteRead: Long, contentLength: Long)
}

/**
 * Listen data transform speed
 * @property responseBody ResponseBody
 * @property progressListener ProgressListener?
 * @property bufferedSource BufferedSource?
 * @constructor
 */
private class ProgressResponseBody(
    private val responseBody: ResponseBody,
    private val progressListener: ProgressListener? = null
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentLength() = responseBody.contentLength()

    override fun contentType(): MediaType? = responseBody.contentType()

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource!!
    }

    private fun source(source: Source) = object : ForwardingSource(source) {
        var totalBytesRead = 0L

        override fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)
            totalBytesRead += if (bytesRead != -1L) bytesRead else 0
            progressListener?.update(totalBytesRead, responseBody.contentLength())
            return bytesRead
        }
    }
}

/**
 * Downloader
 * @property context Context
 * @constructor
 */
class Downloader(private val context: Context) {
    companion object : SingletonHelperArg1<Downloader, Context>(::Downloader)

    private var downloadProgressListener: ((Long, Long, Long) -> Unit)? = null

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addNetworkInterceptor(Interceptor { chain ->
                val startTime = System.currentTimeMillis()
                val request = chain.request()
                val orgResponse = chain.proceed(request)
                orgResponse.newBuilder()
                    .apply {
                        if (orgResponse.body != null) body(ProgressResponseBody(orgResponse.body!!) { readBytes, contentLength ->
                            val elapsedTime = System.currentTimeMillis() - startTime
                            downloadProgressListener?.invoke(readBytes, elapsedTime, contentLength)
                        })
                    }.build()
            })
            .connectTimeout(60_000, TimeUnit.MILLISECONDS)
            .readTimeout(60_000, TimeUnit.MILLISECONDS)
            .build()
    }

    private val downloadPools = hashMapOf<String, Job>()

    private val downloadFileCache = hashMapOf<String, String>()

    private val downloadSp by lazy {
        context.getSharedPreferences("kdownload_range_share", Context.MODE_PRIVATE)
    }

    fun stop(url: String) {
        if (downloadPools.contains(url)) downloadPools[url]?.cancel()
    }

    fun stopAll() = downloadPools.forEach { it.value.cancel() }

    fun isDownloading(url: String) = downloadPools[url]?.isActive ?: false

    fun allDownloadTaskUrls() = downloadPools.keys

    suspend fun download(wrapper: DownloaderWrapper.() -> Unit) {
        val configs = DownloaderWrapper().apply(wrapper)
        val urlSupportRange = downloadSp.contains(configs.url)

        var file: File? = null
        if (urlSupportRange) downloadSp.getString(configs.url, "")?.let { file = File(it) }

        val request = Request.Builder().apply {
            if (!configs.headers.containsKey("Range")) {
                header("Range", "bytes=${file?.tmpFile()?.length() ?: 0}-")
                configs.headers.remove("Range")
            }
            configs.headers.forEach { header(it.key, it.value) }
        }.url(configs.url).build()

        downloadProgressListener = { bytes, elapsed, contentLength -> configs.onDownloadSpeed?.invoke(bytes, elapsed, contentLength) }

        withContext(Dispatchers.IO) {
            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                configs.onDownloadFailed?.invoke(ResponseFailedException())
                return@withContext
            }

            val mimeType = response.contentType()

            if (file == null) {
                val fileName = response.fileName().ifEmpty {
                    configs.onDownloadFailed?.invoke(DownloadException("create file failed"))
                    return@withContext
                }

                file = File(context.filesDir, "cov_download/${fileName}")
            }

            val storeFile = file!!
            if (!urlSupportRange) storeFile.tmpFile().run { if (exists()) delete() }

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