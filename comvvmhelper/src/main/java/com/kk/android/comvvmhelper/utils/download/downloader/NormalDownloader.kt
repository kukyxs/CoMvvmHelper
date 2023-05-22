package com.kk.android.comvvmhelper.utils.download.downloader

import com.kk.android.comvvmhelper.utils.download.DownloadException
import com.kk.android.comvvmhelper.utils.download.contentLength
import com.kk.android.comvvmhelper.utils.download.tmpFile
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.text.DecimalFormat

internal class NormalDownloader : AbsDownloader() {
    private var alreadyDownloaded = false

    override suspend fun download(response: Response, storeFile: File): Job? {
        val body = response.body
        if (body == null) {
            onDownloadFailed?.invoke(DownloadException("response body is null"))
            return null
        }

        beforeDownload(response, storeFile)

        return if (alreadyDownloaded) {
            onProgressChange?.invoke(1f)
            onDownloadCompleted?.invoke()
            null
        } else {
            writeResponseToFile(body, storeFile, response.contentLength())
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun writeResponseToFile(body: ResponseBody, storeFile: File, total: Long): Job {
        return GlobalScope.launch(Dispatchers.IO) {
            val source = body.source()
            if (total <= 0) {
                onDownloadFailed?.invoke(DownloadException("read content length error"))
                return@launch
            }

            val progressFormat = DecimalFormat("0.000")
            val tempFile = storeFile.tmpFile()

            val buffer = ByteArray(1024 * 4)
            var outputStream: FileOutputStream? = null

            try {
                outputStream = FileOutputStream(tempFile)
                var length: Int
                var downloadSize = 0
                while (source.read(buffer).also { length = it } != -1) {
                    if (!isActive) {
                        source.close()
                        outputStream.close()
                        return@launch
                    }

                    outputStream.write(buffer, 0, length)
                    downloadSize += length
                    onProgressChange?.invoke(progressFormat.format(downloadSize * 1f / total).toFloat())
                }

                outputStream.flush()
                tempFile.renameTo(storeFile)
                onDownloadCompleted?.invoke()
            } catch (e: Exception) {
                onDownloadFailed?.invoke(e)
            } finally {
                source.close()
                outputStream?.close()
            }
        }
    }

    private fun beforeDownload(response: Response, storeFile: File) {
        if (storeFile.parentFile?.exists() == false) storeFile.parentFile?.mkdirs()

        if (storeFile.exists()) {
            if (storeFile.length() == response.contentLength()) {
                alreadyDownloaded = true
            } else {
                storeFile.delete()
                storeFile.createTmpDownloadFile()
            }
        } else {
            storeFile.createTmpDownloadFile()
        }
    }

    private fun File.createTmpDownloadFile() = tmpFile().run {
        delete()
        createNewFile().let { RandomAccessFile(this, "rw").setLength(0) }
    }
}