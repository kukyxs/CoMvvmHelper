@file:Suppress("MemberVisibilityCanBePrivate")

package com.kk.android.comvvmhelper.helper

import android.content.Context
import android.util.Log
import com.kk.android.comvvmhelper.utils.openFileByMimeType
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat

/**
 * @author kuky.
 * @description
 */
data class DownloadWrapper(
    var downloadUrl: String = "",
    var storedFilePath: String = "",
    var urlParams: HashMap<String, Any> = hashMapOf(),
    var onProgressChange: suspend (Float) -> Unit = {},
    var onDownloadFinished: suspend (File) -> Unit = {},
    var onDownloadFailed: suspend (Throwable) -> Unit = {}
)

class DownloadHelper private constructor() {

    companion object : SingletonHelperArg0<DownloadHelper>(::DownloadHelper)

    private val mPausedPool = hashMapOf<String, Boolean>()

    private val mCancelPool = hashMapOf<String, Boolean>()

    fun installApk(context: Context, apkFile: File) = context.openFileByMimeType(apkFile)

    fun pauseOrResumeDownload(downloadUrl: String, paused: Boolean) {
        mPausedPool[downloadUrl] = paused
    }

    fun cancelDownload(downloadUrl: String) {
        mCancelPool[downloadUrl] = true
    }

    fun pausedOrResumeAllTask(paused: Boolean) {
        mPausedPool.forEach { entry ->
            pauseOrResumeDownload(entry.key, paused)
        }
    }

    fun cancelAllTask() {
        mCancelPool.forEach { entry ->
            cancelDownload(entry.key)
        }
    }

    suspend fun simpleDownload(wrapper: DownloadWrapper.() -> Unit) {
        val doConfig = DownloadWrapper().apply(wrapper)

        mPausedPool[doConfig.downloadUrl] = false
        mCancelPool[doConfig.downloadUrl] = false

        check(doConfig.storedFilePath.matches(Regex("[a-zA-Z_0-9.\\-()%]+"))) { "illegal file store path" }

        http {
            flowDispatcher = Dispatchers.IO

            baseUrl = doConfig.downloadUrl

            params = doConfig.urlParams

            onSuccess = { realDownload(it, doConfig) }

            onFail = { doConfig.onDownloadFailed(it) }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    internal suspend fun realDownload(response: Response, wrapper: DownloadWrapper) {
        val inputStream = response.body?.byteStream()

        if (inputStream == null || inputStream.available() <= 0) {
            wrapper.onDownloadFailed(IllegalStateException("illegal input stream"))
            return
        }

        val progressFormat = DecimalFormat("00.00")
        val storeFile = File(wrapper.storedFilePath)

        if (storeFile.parentFile?.exists() == false) {
            storeFile.parentFile?.mkdirs()
        }

        if (!storeFile.exists()) {
            storeFile.createNewFile()
        }

        val buffer = ByteArray(1024)
        var fileOutputStream: FileOutputStream? = null

        try {
            var sum = 0L
            val total = calculateContentLength(wrapper.downloadUrl)

            if (total == -1L) {
                Log.e("Download", "error on get contentLength for download url: ${wrapper.downloadUrl}, maybe service internal error")
            }

            fileOutputStream = FileOutputStream(storeFile)
            var length = inputStream.read(buffer)

            while (length != -1) {
                if (mCancelPool[wrapper.downloadUrl] == true) {
                    storeFile.delete()
                    break
                }

                while (mPausedPool[wrapper.downloadUrl] == true) {
                    try {
                        Thread.sleep(100)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                fileOutputStream.write(buffer, 0, length)
                length = inputStream.read(buffer)
                sum += length
                val progress = sum * 1f / total
                wrapper.onProgressChange(progressFormat.format(progress).toFloat())
            }

            fileOutputStream.flush()
            wrapper.onProgressChange.invoke(1f)
            wrapper.onDownloadFinished.invoke(storeFile)
        } catch (e: Exception) {
            e.printStackTrace()
            wrapper.onDownloadFailed(e)
        } finally {
            inputStream.close()
            fileOutputStream?.close()
        }
    }

    private fun calculateContentLength(downloadUrl: String): Long {
        val request = Request.Builder().url(downloadUrl).build()

        try {
            val resp = OkHttpClient.Builder().build().newCall(request).execute()
            if (resp.isSuccessful) {
                val contentLength = resp.body?.contentLength() ?: -1
                resp.close()
                return contentLength
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }
}