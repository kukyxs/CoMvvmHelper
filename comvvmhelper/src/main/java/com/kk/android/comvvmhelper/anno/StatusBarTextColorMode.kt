package com.kk.android.comvvmhelper.anno

import androidx.annotation.IntDef

/**
 * @author kuky.
 * @description
 */
@IntDef(value = [StatusBarTextColorMode.LIGHT, StatusBarTextColorMode.DARK])
@Retention(AnnotationRetention.SOURCE)
annotation class StatusBarTextColorMode {
    companion object {
        const val LIGHT = 0
        const val DARK = 1
    }
}