package com.kk.android.comvvmhelper.anno

import android.view.Gravity

/**
 * @author kuky.
 * @description
 */

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class DialogConfig(
    val widthType: Int = DialogSizeType.WRAP_SIZE,
    val heightType: Int = DialogSizeType.WRAP_SIZE,
    val widthFraction: Float = 0.75f,
    val heightFraction: Float = 0f, // fraction = 0f => wrap_content
    val gravity: Int = Gravity.CENTER,
    val backgroundColor: String = "#00000000"
)
