package com.kk.android.comvvmhelper.entity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.WindowManager

/**
 * @author kuky.
 * @description
 * @param dialogWidth the width of dialog
 * @param dialogHeight the height of dialog
 * @param dialogGravity dialog display Mode
 */
data class DialogDisplayConfig(
    val dialogWidth: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    val dialogHeight: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    val dialogGravity: Int = Gravity.CENTER,
    val dialogBackground: Drawable = ColorDrawable(Color.TRANSPARENT)
)