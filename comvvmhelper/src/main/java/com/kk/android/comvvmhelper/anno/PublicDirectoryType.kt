package com.kk.android.comvvmhelper.anno

/**
 * @author kuky.
 * @description
 */
sealed class PublicDirectoryType(val value: Int) {
    object MUSICS : PublicDirectoryType(0)
    object MOVIES : PublicDirectoryType(1)
    object PICTURES : PublicDirectoryType(2)
    object DOWNLOADS : PublicDirectoryType(3)
}