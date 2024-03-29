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
@Deprecated("use File.copyTo replaced", level = DeprecationLevel.WARNING)
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
    relativePath: String = "", mimeType: String? = null,
    overwriteIfTargetFileExists: Boolean = false, copyFailed: ((Throwable) -> Unit)? = null
) {
    copyFileToPublic(oriPrivateFile, displayName, relativePath, mimeType, PublicDirectoryType.PICTURES, overwriteIfTargetFileExists, copyFailed)
}

@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToPublicMoveOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null,
    overwriteIfTargetFileExists: Boolean = false, copyFailed: ((Throwable) -> Unit)? = null
) {
    copyFileToPublic(oriPrivateFile, displayName, relativePath, mimeType, PublicDirectoryType.MOVIES, overwriteIfTargetFileExists, copyFailed)
}

@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToPublicMusicOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null,
    overwriteIfTargetFileExists: Boolean = false, copyFailed: ((Throwable) -> Unit)? = null
) {
    copyFileToPublic(oriPrivateFile, displayName, relativePath, mimeType, PublicDirectoryType.MUSICS, overwriteIfTargetFileExists, copyFailed)
}

@TargetApi(Build.VERSION_CODES.Q)
fun Context.copyFileToDownloadsOnQ(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null,
    overwriteIfTargetFileExists: Boolean = false, copyFailed: ((Throwable) -> Unit)? = null
) {
    copyFileToPublic(oriPrivateFile, displayName, relativePath, mimeType, PublicDirectoryType.DOWNLOADS, overwriteIfTargetFileExists, copyFailed)
}

internal fun realRelativePath(
    relativePath: String = "",
    copyTarget: PublicDirectoryType = PublicDirectoryType.DOWNLOADS
) = if (relativePath.isBlank()) "" else when (copyTarget) {
    PublicDirectoryType.PICTURES -> Environment.DIRECTORY_PICTURES
    PublicDirectoryType.DOWNLOADS -> Environment.DIRECTORY_DOWNLOADS
    PublicDirectoryType.MOVIES -> Environment.DIRECTORY_MOVIES
    PublicDirectoryType.MUSICS -> Environment.DIRECTORY_MUSIC
} + File.separator + relativePath

fun Context.copyFileToPublic(
    oriFile: File, displayName: String, relativePath: String = "",
    mimeType: String? = null, copyTarget: PublicDirectoryType = PublicDirectoryType.DOWNLOADS,
    overwriteIfTargetFileExists: Boolean = false, copyFailed: ((Throwable) -> Unit)? = null
): File? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        copyFileToPublicDirectory(oriFile, displayName, relativePath, mimeType, copyTarget, copyFailed)
    } else {
        val targetDir = when (copyTarget) {
            PublicDirectoryType.DOWNLOADS -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            PublicDirectoryType.MOVIES -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            PublicDirectoryType.MUSICS -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
            PublicDirectoryType.PICTURES -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        }

        val storeFile = File(targetDir, "$relativePath/$displayName")
        try {
            oriFile.copyTo(storeFile, overwriteIfTargetFileExists)
        } catch (e: Exception) {
            copyFailed?.invoke(e)
            null
        }
    }
}

@TargetApi(Build.VERSION_CODES.Q)
internal fun Context.copyFileToPublicDirectory(
    oriPrivateFile: File, displayName: String,
    relativePath: String = "", mimeType: String? = null,
    copyTarget: PublicDirectoryType = PublicDirectoryType.DOWNLOADS,
    copyFailed: ((Throwable) -> Unit)? = null
): File? {
    val externalState = Environment.getExternalStorageState()

    val realReactivePath = realRelativePath(relativePath, copyTarget)
    val copyValues = ContentValues().apply {
        put(MediaStore.MediaColumns.TITLE, displayName)
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType ?: getMimeTypeByFile(oriPrivateFile.absolutePath))
        put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
        if (realReactivePath.isNotBlank()) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, realReactivePath)
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
    }

    if (uri != null) {
        val buffer = ByteArray(1024)
        var bos: BufferedOutputStream? = null
        val outputStream = contentResolver.openOutputStream(uri) ?: return null
        val bis = BufferedInputStream(FileInputStream(oriPrivateFile))

        try {
            bos = BufferedOutputStream(outputStream)
            var length = bis.read(buffer)

            while (length != -1) {
                bos.write(buffer, 0, length)
                bos.flush()
                length = bis.read(buffer)
            }

            return File(Environment.getExternalStorageDirectory(), realReactivePath + File.separator + displayName)
        } catch (e: Exception) {
            e.printStackTrace()
            copyFailed?.invoke(e)
            return null
        } finally {
            bis.close()
            bos?.close()
        }
    } else {
        copyFailed?.invoke(NullPointerException("Uri is null"))
        return null
    }
}