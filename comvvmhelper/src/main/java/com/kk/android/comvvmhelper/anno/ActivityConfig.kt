package com.kk.android.comvvmhelper.anno

/**
 * @author kuky.
 * @description
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ActivityConfig(
    val windowState: Int = WindowState.NORMAL,
    val statusBarColorString: String = "",
    /** only worked above Android M */
    val statusBarTextColorMode: Int = StatusBarTextColorMode.LIGHT,
    /** when status bar is transparent, window content need up to status bar */
    val contentUpToStatusBarWhenTransparent: Boolean = false
)