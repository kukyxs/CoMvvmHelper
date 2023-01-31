package com.kk.android.comvvmhelper.extension

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

/**
 * @author kuky.
 * @description
 */
fun Context.stringValue(@StringRes strRes: Int) = resources.getString(strRes)

fun Context.drawableValue(@DrawableRes drawRes: Int) =
    ContextCompat.getDrawable(this, drawRes) ?: ColorDrawable(Color.TRANSPARENT)

fun Context.colorValue(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.dimenValue(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

/**
 * example: get an drawable resource ic_launcher.png from drawable direction
 *
 * ```kotlin
 *    val iconLauncher = getResourceFromRawDirectory("ic_launcher.png", "drawable")
 * ```
 */
@SuppressLint("DiscouragedApi")
fun Context.getResourceFromRawDirectory(resourceName: String, directoryName: String) =
    resources.getIdentifier(resourceName, directoryName, packageName)
