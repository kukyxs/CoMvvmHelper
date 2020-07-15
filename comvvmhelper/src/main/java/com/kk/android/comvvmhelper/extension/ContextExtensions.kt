package com.kk.android.comvvmhelper.extension

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * @author kuky.
 * @description
 */
fun Context.stringValue(@StringRes strRes: Int) = resources.getString(strRes)

fun Context.drawableValue(@DrawableRes drawRes: Int) = ContextCompat.getDrawable(this, drawRes)

fun Context.colorValue(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.dimenValue(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun Context.getResourceFromResourceDirectory(resourceName: String, directoryName: String) =
    resources.getIdentifier(resourceName, directoryName, packageName)

fun FragmentActivity.windowFullScreen() =
    window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

fun FragmentActivity.transparentBars(transparentNavigationBar: Boolean = false) {
    var flag = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    if (transparentNavigationBar) flag = flag or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    window?.decorView?.systemUiVisibility = flag
    window?.statusBarColor = Color.TRANSPARENT
    if (transparentNavigationBar) window?.navigationBarColor = Color.TRANSPARENT
    if (this is AppCompatActivity) supportActionBar?.hide()
}
