package com.kk.android.comvvmhelper.anno

import androidx.annotation.IntDef

/**
 * @author kuky.
 * @description
 */
@IntDef(value = [PublicDirectoryType.MUSICS, PublicDirectoryType.MOVIES, PublicDirectoryType.PICTURES, PublicDirectoryType.DOWNLOADS])
@Retention(AnnotationRetention.SOURCE)
annotation class PublicDirectoryType {
    companion object {
        const val MUSICS = 0 // Environment.DIRECTORY_MUSIC
        const val MOVIES = 1 //  Environment.DIRECTORY_MOVIES
        const val PICTURES = 2 // Environment.DIRECTORY_PICTURES
        const val DOWNLOADS = 3 // Environment.DIRECTORY_DOWNLOADS
    }
}