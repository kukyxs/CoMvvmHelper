package com.kk.android.comvvmhelper.anno

import androidx.annotation.IntDef

/**
 * @author kuky.
 * @description
 */
@IntDef(value = [WindowState.TRANSPARENT_STATUS_BAR, WindowState.NORMAL])
@Retention(AnnotationRetention.SOURCE)
annotation class WindowState {
    companion object {
        const val TRANSPARENT_STATUS_BAR = 0
        const val NORMAL = 1
    }
}