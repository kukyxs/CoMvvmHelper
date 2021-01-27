package com.kuky.comvvmhelper.entity

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity

/**
 * @author kuky.
 * @description
 */
data class GuideDisplay(
    val description: String,
    val headIcon: Drawable,
    val targetDestination: Class<out AppCompatActivity>,
    val showSwitch: Boolean = false,
    val switchDescription: String = ""
)