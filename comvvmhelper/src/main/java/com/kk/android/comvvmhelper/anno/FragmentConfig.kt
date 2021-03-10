package com.kk.android.comvvmhelper.anno

/**
 * @author kuky.
 * @description
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class FragmentConfig(
    /** need create koin scope for fragment */
    val enableKoinScope: Boolean = false
)
