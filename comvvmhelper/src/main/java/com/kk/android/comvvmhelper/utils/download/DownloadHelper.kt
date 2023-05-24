package com.kk.android.comvvmhelper.utils.download

import android.os.Environment
import android.webkit.MimeTypeMap
import com.kk.android.comvvmhelper.anno.PublicDirectoryType
import com.kk.android.comvvmhelper.helper.realRelativePath
import okhttp3.Response
import okhttp3.internal.toLongOrDefault
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile

fun Response.contentType() = headers["Content-Type"] ?: ""

fun Response.supportRanges() = headers["Accept-Ranges"]?.let { it == "bytes" } ?: false

fun Response.fileName() = contentDisposition()
    .ifEmpty { request.url.toString().fileNameFromUrl() }
    .ifEmpty { "${System.currentTimeMillis()}.${MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType())}" }
    .replace("\"", "")

fun File.tmpFile() = File("$canonicalPath.tmp")

fun getPublicFile(
    displayName: String, relativePath: String = "",
    copyTarget: PublicDirectoryType = PublicDirectoryType.DOWNLOADS
) = File(Environment.getExternalStorageDirectory(), realRelativePath(relativePath, copyTarget) + File.separator + displayName)

fun String.getUniqueFileName(dirName: String): String {
    val downloadDir = File(dirName)
    var file = File(downloadDir, this)
    if (!file.exists()) return this

    val suffix = if (contains(".")) substring(lastIndexOf(".") + 1) else ""
    val baseName = replace(".$suffix", "")

    var appendChar = 1
    var newFileName: String
    do {
        newFileName = String.format("%s(%d).%s", baseName, appendChar++, suffix)
        file = File(downloadDir, newFileName)
    } while (file.exists())

    return newFileName
}

internal fun File.inUse(): Boolean {
    if (!exists()) return false

    var inUse = false
    val channel = RandomAccessFile(this, "rw").channel

    val lock = try {
        channel.tryLock()
    } catch (e: IOException) {
        null
    }

    if (lock != null) {
        inUse = true
        lock.release()
        channel.close()
    }

    return inUse
}

internal fun Response.mimeType(): String = headers["Content-Type"] ?: ""

internal fun Response.contentLength(): Long =
    headers["Content-Length"]?.toLongOrDefault(-1) ?: -1

internal fun Response.contentDisposition(): String {
    val contentDisposition = headers["Content-Disposition"]?.lowercase() ?: return ""
    val fileNameParts = contentDisposition.split(";").map { it.trim() }
        .filter { it.startsWith("filename=") }.ifEmpty { return "" }
    return fileNameParts.first().substring(9).replace("/", "_")
}

internal fun String.fileNameFromUrl(): String {
    var name = ifEmpty { return "" }
    if (name.contains("#")) name = name.substring(0, name.lastIndexOf("#"))
    if (name.contains("?")) name = name.substring(0, name.lastIndexOf("?"))
    if (name.contains("/")) name = name.substring(name.lastIndexOf("/") + 1)
    return if (name.matches(Regex("[a-zA-Z_0-9.:\\-()%]+"))) name else ""
}