package com.kk.android.comvvmhelper.utils

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.*
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes

/**
 * @author kuky.
 * @description
 */
@Suppress("DEPRECATION")
fun Activity.setColorForStatusBar(@ColorInt color: Int) {
    window.let {
        it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        it.statusBarColor = color
        it.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
            .getChildAt(0)?.let { child ->
                child.fitsSystemWindows = false
                ViewCompat.requestApplyInsets(child)
            }
    }
}

@Suppress("DEPRECATION")
fun Activity.translucentStatusBar(contentToStatusBar: Boolean) {
    window.let {
        it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        actionsByR({
            contentToStatusBar.yes {
                it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                it.statusBarColor = Color.TRANSPARENT
                it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }.otherwise {
                it.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }, {
            it.statusBarColor = Color.TRANSPARENT
            it.setDecorFitsSystemWindows(!contentToStatusBar)
        })

        it.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
            .getChildAt(0)?.let { child ->
                child.fitsSystemWindows = false
                ViewCompat.requestApplyInsets(child)
            }
    }
}

@Suppress("DEPRECATION")
@TargetApi(Build.VERSION_CODES.M)
fun Activity.setStatusBarLightMode() {
    actionsByR({
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }, {
        window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    })
}

@Suppress("DEPRECATION")
@TargetApi(Build.VERSION_CODES.M)
fun Activity.setStatusBarDarkMode() {
    actionsByR({
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }, {
        window.insetsController?.setSystemBarsAppearance(
            0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    })
}