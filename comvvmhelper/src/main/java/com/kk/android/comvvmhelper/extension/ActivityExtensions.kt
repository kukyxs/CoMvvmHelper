package com.kk.android.comvvmhelper.extension

import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import com.kk.android.comvvmhelper.utils.actionsByR

/**
 * @author kuky.
 * @description
 */
@Suppress("DEPRECATION")
fun FragmentActivity.normalWindow() {
    actionsByR({
        window.decorView.systemUiVisibility = 0
    }, {
        window.insetsController?.setSystemBarsAppearance(0, 0)
    })
}

@Suppress("DEPRECATION")
fun FragmentActivity.hideStatusBar() {
    actionsByR({
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_FULLSCREEN
    }, {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    })

    actionBar?.hide()
}

@Suppress("DEPRECATION")
fun FragmentActivity.hideNavigationBar() {
    actionsByR({
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }, {
        window.insetsController?.hide(WindowInsets.Type.navigationBars())
    })
}

@Suppress("DEPRECATION")
fun FragmentActivity.fullScreen() {
    actionsByR({
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }, {
        window.insetsController?.run {
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsets.Type.statusBars())
            hide(WindowInsets.Type.navigationBars())
            WindowInsets.Builder().build().run {
                getInsetsIgnoringVisibility(WindowInsets.Type.statusBars())
                getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars())
            }
        }
    })
    actionBar?.hide()
}

@Suppress("DEPRECATION")
fun FragmentActivity.exitFullScreen() {
    actionsByR({
        window.decorView.systemUiVisibility = 0
    }, {
        window.insetsController?.run {
            show(WindowInsets.Type.statusBars())
            show(WindowInsets.Type.navigationBars())
        }
    })

    actionBar?.show()
}

fun AppCompatActivity.replaceActionBar(toolbar: Toolbar) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(true)
        setDisplayShowTitleEnabled(true)
    }
}
