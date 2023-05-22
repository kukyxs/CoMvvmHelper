package com.kk.android.comvvmhelper.utils.download.downloader

import kotlinx.coroutines.Job
import okhttp3.Response
import java.io.File

internal abstract class AbsDownloader {
    var onProgressChange: (suspend (Float) -> Unit)? = null
    var onDownloadFailed: (suspend (Throwable) -> Unit)? = null
    var onDownloadCompleted: (suspend () -> Unit)? = null

    abstract suspend fun download(response: Response, storeFile: File): Job?
}