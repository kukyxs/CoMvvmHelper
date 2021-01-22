package com.kk.android.comvvmhelper.helper

import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes
import com.kk.android.comvvmhelper.utils.getMimeTypeByFile
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream

/**
 * @author kuky.
 * @description
 */

enum class CopyTarget {
    MUSICS, MOVIES, DOWNLOADS, PICTURES
}

@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToPublicPictureOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null
) {
    copyFileToPublicDirectory(oriPrivateFile, displayName, relativePath, mimeType, CopyTarget.PICTURES)
}

@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToPublicMoveOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null
) {
    copyFileToPublicDirectory(oriPrivateFile, displayName, relativePath, mimeType, CopyTarget.MOVIES)
}

@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToPublicMusicOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null
) {
    copyFileToPublicDirectory(oriPrivateFile, displayName, relativePath, mimeType, CopyTarget.MUSICS)
}

@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToDownloadsOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null
) {
    copyFileToPublicDirectory(oriPrivateFile, displayName, relativePath, mimeType, CopyTarget.DOWNLOADS)
}

@TargetApi(Build.VERSION_CODES.Q)
internal fun Context.copyFileToPublicDirectory(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null, copyTarget: CopyTarget
) {
    val externalState = Environment.getExternalStorageState()
    val copyValues = ContentValues().apply {
        put(MediaStore.MediaColumns.TITLE, displayName)
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType ?: getMimeTypeByFile(oriPrivateFile.absolutePath))

        put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())

        if (relativePath.isBlank()) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        }
    }

    val uri = when (copyTarget) {
        CopyTarget.PICTURES -> contentResolver.insert(
            (externalState == Environment.MEDIA_MOUNTED)
                .yes { MediaStore.Images.Media.EXTERNAL_CONTENT_URI }
                .otherwise { MediaStore.Images.Media.INTERNAL_CONTENT_URI }, copyValues
        )

        CopyTarget.MOVIES -> contentResolver.insert(
            (externalState == Environment.MEDIA_MOUNTED)
                .yes { MediaStore.Video.Media.EXTERNAL_CONTENT_URI }
                .otherwise { MediaStore.Video.Media.INTERNAL_CONTENT_URI }, copyValues
        )

        CopyTarget.MUSICS -> contentResolver.insert(
            (externalState == Environment.MEDIA_MOUNTED)
                .yes { MediaStore.Audio.Media.EXTERNAL_CONTENT_URI }
                .otherwise { MediaStore.Audio.Media.INTERNAL_CONTENT_URI }, copyValues
        )

        CopyTarget.DOWNLOADS -> contentResolver.insert(
            (externalState == Environment.MEDIA_MOUNTED)
                .yes { MediaStore.Downloads.EXTERNAL_CONTENT_URI }
                .otherwise { MediaStore.Downloads.INTERNAL_CONTENT_URI }, copyValues
        )
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