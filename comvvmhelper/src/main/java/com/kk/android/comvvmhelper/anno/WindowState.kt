package com.kk.android.comvvmhelper.anno

import androidx.annotation.IntDef

/**
 * @author kuky.
 * @description
 */
@IntDef(value = [WindowState.TRANSPARENT_STATUS_BAR, WindowState.NORMAL, WindowState.TRANSLUCENT_STATUS_BAR])
@Retention(AnnotationRetention.SOURCE)
annotation class WindowState {
    companion object {
        const val TRANSPARENT_STATUS_BAR = 0
        const val NORMAL = 1

        @Deprecated("not work any more, use [TRANSPARENT_STATUS_BAR] replaced")
        const val TRANSLUCENT_STATUS_BAR = 2
    }
}