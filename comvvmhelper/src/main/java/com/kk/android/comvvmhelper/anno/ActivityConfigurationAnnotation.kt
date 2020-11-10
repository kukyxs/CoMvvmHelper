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
    @ColorInt val statusBarColor: Int = Color.TRANSPARENT,
    /** only worked above Android M */
    val statusBarTextColorMode: StatusBarTextColorMode = StatusBarTextColorMode.Light
)

enum class WindowState {
    TRANSLUCENT_STATUS_BAR, NORMAL
}

enum class StatusBarTextColorMode {
    Light, Dark
}