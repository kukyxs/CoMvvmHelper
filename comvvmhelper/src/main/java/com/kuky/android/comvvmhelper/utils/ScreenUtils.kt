package com.kuky.android.comvvmhelper.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kuky.android.comvvmhelper.R

/**
 * @author kuky.
 * @description
 */
private const val DEFAULT_STATUS_BAR_ALPHA = 112
private val FAKE_STATUS_BAR_VIEW_ID = R.id.status_fake_status_bar_view

fun Context.getScreenWidth() = resources.displayMetrics.widthPixels

fun Context.getScreenHeight() = resources.displayMetrics.heightPixels

fun Context.getScreenDensity() = resources.displayMetrics.density

fun Context.dip2px(dpValue: Float) = dpValue * getScreenDensity() + 0.5f

fun Context.sp2px(spValue: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, resources.displayMetrics)

fun Context.px2dip(pxValue: Float) = pxValue / getScreenDensity() + 0.5f

fun Context.px2sp(pxValue: Float) = (pxValue / resources.displayMetrics.scaledDensity)

fun Context.getStatusBarHeight() =
    resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))

fun AppCompatActivity.replaceActionBar(toolbar: Toolbar) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(true)
        setDisplayShowTitleEnabled(true)
    }
}

/**
 * 为 status bar 设置颜色
 */
fun setColorForStatusBar(
    activity: Activity, @ColorInt color: Int,
    @IntRange(from = 0, to = 255) statusBarAlpha: Int = DEFAULT_STATUS_BAR_ALPHA
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.window?.let {
            it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            it.statusBarColor = calculateStatusColor(color, statusBarAlpha)
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val decorView = activity.window.decorView as ViewGroup
        val fakeStatusBar = decorView.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
        fakeStatusBar.let {
            if (it != null) {
                if (it.visibility == View.GONE) it.visibility = View.VISIBLE
                it.setBackgroundColor(calculateStatusColor(color, statusBarAlpha))
            } else {
                decorView.addView(createStatusBarView(activity, color, statusBarAlpha))
            }
        }
        setRootView(activity)
    }
}

/**
 * 设置状态栏文字为深色
 */
@TargetApi(Build.VERSION_CODES.M)
fun setStatusBarLightMode(activity: Activity) {
    setMiUiStatusBarDarkMode(activity)
    activity.window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}

/**
 * 设置状态栏文字为浅色
 */
@TargetApi(Build.VERSION_CODES.M)
fun setStatusBarDarkMode(activity: Activity) {
    setMiUiStatusBarDarkMode(activity, true)
    activity.window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}

/**
 * 适配 MiUi V6 以上状态栏
 */
@SuppressLint("PrivateApi")
private fun setMiUiStatusBarDarkMode(activity: Activity, darkMode: Boolean = false) {
    val clazz = activity.window.javaClass

    try {
        val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        val darkModeFlag = field.getInt(layoutParams)
        val extraFlagField = clazz.getMethod(
            "setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType
        )
        extraFlagField.invoke(activity.window, if (darkMode) darkModeFlag else 0, darkModeFlag)
    } catch (e: Exception) {
        Log.e("StatusBarUtils", "MiUiDarkMode", e)
    }
}

/**
 * 创建一个虚拟的 status bar
 */
private fun createStatusBarView(
    activity: Activity, @ColorInt color: Int,
    @IntRange(from = 0, to = 255) alpha: Int = 0
) = View(activity).apply {
    layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, activity.getStatusBarHeight()
    )
    setBackgroundColor(calculateStatusColor(color, alpha))
    id = FAKE_STATUS_BAR_VIEW_ID
}

/**
 * 设置根布局参数
 */
private fun setRootView(activity: Activity) =
    activity.findViewById<ViewGroup>(android.R.id.content)
        .let { parent ->
            for (i in 0 until parent.childCount) {
                parent.getChildAt(i).let {
                    if (it is ViewGroup) {
                        it.fitsSystemWindows = true
                        it.clipToPadding = true
                    }
                }
            }
        }

/**
 * 根据传入色值和透明度计算颜色值
 */
private fun calculateStatusColor(
    @ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int
) = if (alpha == 0) color else {
    val a = 1 - alpha / 255f
    val red = color shr 16 and 0xff
    val green = color shr 8 and 0xff
    val blue = color and 0xff
    (0xff shr 24) or (((red * a + 0.5).toInt()) shr 16) or (((green * a + 0.5).toInt()) shr 8) or ((blue * a + 0.5).toInt())
}