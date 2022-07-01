package com.kk.android.comvvmhelper.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File
import java.util.*

/**
 * @author kuky.
 * @description
 */
fun getMimeTypeByFile(file: String): String {
    return file.let {
        if (it.contains("."))
            MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(
                    it.split(".").last().lowercase(Locale.getDefault())
                ) ?: "*/*"
        else "*/*"
    }
}

fun Context.openFileByMimeType(
    file: File, authority: String? = null,
    viewEmptyFile: Boolean = true,
    handleUnknownBySystem: Boolean = true,
    unknownMimeType: ((File) -> Unit)? = null
) {
    if (file.length() <= 0L && viewEmptyFile.not()) return

    val mimeType = getMimeTypeByFile(file.name)

    if (mimeType == "*/*" && handleUnknownBySystem.not()) {
        unknownMimeType?.invoke(file) ?: return
        return
    }

    openFileByMimeType(file, mimeType, authority)
}

fun Context.openFileByMimeType(file: File, mimeType: String, authority: String? = null) {
    try {
        val uri = if (Build.VERSION.SDK_INT > 23)
            FileProvider.getUriForFile(this, authority ?: "$packageName.fileprovider", file)
        else Uri.fromFile(file)

        startActivity(Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}