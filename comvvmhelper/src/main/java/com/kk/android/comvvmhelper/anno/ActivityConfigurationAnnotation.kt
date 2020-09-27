package com.kk.android.comvvmhelper.anno

import android.graphics.Color
import androidx.annotation.ColorInt

/**
 * @author kuky.
 * @description
 */

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ActivityConfig(
    val windowState: WindowState = WindowState.NORMAL,
    @ColorInt val statusBarColor: Int = Color.TRANSPARENT
)

enum class WindowState {
    TRANSLUCENT_STATUS_BAR, NORMAL
}