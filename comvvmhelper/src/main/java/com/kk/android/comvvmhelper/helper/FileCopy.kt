package com.kk.android.comvvmhelper.helper

import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.kk.android.comvvmhelper.anno.PublicDirectoryType
import com.kk.android.comvvmhelper.utils.getMimeTypeByFile
import java.io.*

/**
 * @author kuky.
 * @description
 */
fun copyFileBelowQ(srcFile: File, dstFile: File) {
    val inputStream = FileInputStream(srcFile)
    val outputStream = FileOutputStream(dstFile)
    try {
        val buffer = ByteArray(1024)
        var length = inputStream.read(buffer)

        while (length != -1) {
            outputStream.write(buffer, 0, length)
            outputStream.flush()
            length = inputStream.read(buffer)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        inputStream.close()
        outputStream.close()
    }
}

////////////////////////////////////////////////////////////////////////////
// FileCopyForQAndAbove(SAF) ///////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToPublicPictureOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null
) {
    copyFileToPublicDirectory(oriPrivateFile, displayName, relativePath, mimeType, PublicDirectoryType.PICTURES)
}

@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToPublicMoveOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null
) {
    copyFileToPublicDirectory(oriPrivateFile, displayName, relativePath, mimeType, PublicDirectoryType.MOVIES)
}

@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToPublicMusicOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null
) {
    copyFileToPublicDirectory(oriPrivateFile, displayName, relativePath, mimeType, PublicDirectoryType.MUSICS)
}

@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToDownloadsOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null
) {
    copyFileToPublicDirectory(oriPrivateFile, displayName, relativePath, mimeType, PublicDirectoryType.DOWNLOADS)
}

@TargetApi(Build.VERSION_CODES.Q)
internal fun Context.copyFileToPublicDirectory(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null, copyTarget: Int
) {
    val externalState = Environment.getExternalStorageState()
    val copyValues = ContentValues().apply {
        put(MediaStore.MediaColumns.TITLE, displayName)
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType ?: getMimeTypeByFile(oriPrivateFile.absolutePath))
        put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
        if (relativePath.isNotBlank()) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        }
    }

    val uri = when (copyTarget) {
        PublicDirectoryType.PICTURES -> contentResolver.insert(
            if (externalState == Environment.MEDIA_MOUNTED) {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            }, copyValues
        )

        PublicDirectoryType.MOVIES -> contentResolver.insert(
            if (externalState == Environment.MEDIA_MOUNTED) {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Video.Media.INTERNAL_CONTENT_URI
            }, copyValues
        )

        PublicDirectoryType.MUSICS -> contentResolver.insert(
            if (externalState == Environment.MEDIA_MOUNTED) {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI
            }, copyValues
        )

        PublicDirectoryType.DOWNLOADS -> contentResolver.insert(
            if (externalState == Environment.MEDIA_MOUNTED) {
                MediaStore.Downloads.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Downloads.INTERNAL_CONTENT_URI
            }, copyValues
        )

        else -> throw IllegalArgumentException("not support type")
    }

    uri?.run {
        val buffer = ByteArray(1024)
        var bos: BufferedOutputStream? = null
        val outputStream = contentResolver.openOutputStream(this) ?: return
        val bis = BufferedInputStream(FileInputStream(oriPrivateFile))

        try {
            bos = BufferedOutputStream(outputStream)
            var length = bis.read(buffer)

            while (length != -1) {
                bos.write(buffer, 0, length)
                bos.flush()
                length = bis.read(buffer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bis.close()
            bos?.close()
        }
    }
}