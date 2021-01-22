package com.kk.android.comvvmhelper.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File
import java.util.*

/**
 * @author kuky.
 * @description
 */
fun getMimeTypeByFile(file: String): String {
    return if (file.contains(".")) {
        when (file.split(".").last().toLowerCase(Locale.getDefault())) {
            // apk file
            "apk" -> "application/vnd.android.package-archive"

            // document type
            "abw" -> "application/x-abiword"
            "arc" -> "application/x-freearc"
            "azw" -> "application/vnd.amazon.ebook"
            "xml" -> "text/xml"
            "html" -> "text/html"
            "pdf" -> "application/pdf"
            "txt", "log" -> "text/plain"
            "doc" -> "application/msword"
            "ppt", "pps" -> "application/vnd.ms-powerpoint"
            "xls" -> "application/vnd.ms-excel application/x-excel"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            "ppsx" -> "application/vnd.openxmlformats-officedocument.presentationml.slideshow"

            // image type
            "gif" -> "image/gif"
            "png" -> "image/png"
            "tif", "tiff" -> "image/tiff"
            "jpe", "jpg", "jpeg" -> "image/jpeg"
            "wbmp" -> "image/vnd.wap.wbmp"

            // compressed type
            "z" -> "application/x-compress"
            "tgz" -> "application/x-compressed"
            "zip" -> "application/x-zip-compressed"
            "7z" -> "application/application/x-7z-compressed"

            // video/audio type
            "mp4" -> "video/mp4"
            "3gp" -> "video/3gpp"
            "wmv" -> "video/x-ms-wmv"
            "wav" -> "audio/wav"
            "amr" -> "audio/amr-wb"
            "wma" -> "audio/x-ms-wma"
            "mp3", "mpeg", "mpg" -> "audio/mpeg"
            "mid", "midi" -> "audio/midi"
            "aac" -> "audio/aac"
            "avi" -> "video/x-msvideo"

            // other type
            "rtf" -> "application/rtf"
            "tar" -> "application/x-tar"
            "wps" -> "application/vnd.ms-works"
            "jar" -> "application/java-archive"
            "msg" -> "application/vnd.ms-outlook"
            "rar", "exe" -> "application/octet-stream"
            else -> "*/*"
        }
    } else "*/*"
}

fun Context.openFileByMimeType(file: File, authority: String? = null, unknownMimeType: ((File) -> Unit)? = null) {
    if (file.length() <= 0L) return

    val mimeType = getMimeTypeByFile(file.name)

    if (mimeType == "*/*") unknownMimeType?.invoke(file) ?: return

    openFileByMimeType(file, mimeType, authority)
}

fun Context.openFileByMimeType(file: File, mimeType: String, authority: String? = null) {
    try {
        val uri = if (Build.VERSION.SDK_INT > 23)
            FileProvider.getUriForFile(this, authority ?: "$packageName.FileProvider", file)
        else Uri.fromFile(file)

        startActivity(Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}