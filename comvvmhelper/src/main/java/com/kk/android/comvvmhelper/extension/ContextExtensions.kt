package com.kk.android.comvvmhelper.extension

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
    ContextCompat.getDrawable(this, drawRes) ?: ColorDrawable(Color.TRANSPARENT).apply {
        setBounds(0, 0, 1, 1)
    }

fun Context.colorValue(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.dimenValue(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun Context.getResourceFromRawDirectory(resourceName: String, directoryName: String) =
    resources.getIdentifier(resourceName, directoryName, packageName)
