package com.kk.android.comvvmhelper.utils

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes

/**
 * @author kuky.
 * @description
 */
fun Activity.setColorForStatusBar(@ColorInt color: Int) {
    window?.let {
        it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        it.statusBarColor = color

        it.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
            .getChildAt(0)?.let { child ->
                child.fitsSystemWindows = false
                ViewCompat.requestApplyInsets(child)
            }
    }
}

fun Activity.translucentStatusBar(hideStatusBarBackground: Boolean) {
    window?.let {
        it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        hideStatusBarBackground.yes {
            it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            it.statusBarColor = Color.TRANSPARENT
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }.otherwise {
            it.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }

        it.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
            .getChildAt(0)?.let { child ->
                child.fitsSystemWindows = false
                ViewCompat.requestApplyInsets(child)
            }
    }
}

@TargetApi(Build.VERSION_CODES.M)
fun Activity.setStatusBarLightMode() {
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}

@TargetApi(Build.VERSION_CODES.M)
fun Activity.setStatusBarDarkMode() {
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}