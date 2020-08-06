@file:Suppress("MemberVisibilityCanBePrivate")

package com.kk.android.comvvmhelper.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
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
import com.kk.android.comvvmhelper.R
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes

/**
 * @author kuky.
 * @description
 */
private const val DEFAULT_STATUS_BAR_ALPHA = 112

private val FAKE_STATUS_BAR_VIEW_ID = R.id.status_fake_status_bar_view

val screenWidth = Resources.getSystem().displayMetrics.widthPixels

val screenHeight = Resources.getSystem().displayMetrics.heightPixels

val screenDensity = Resources.getSystem().displayMetrics.density

fun Float.dp2px() = screenDensity * this + 0.5f

fun Float.sp2px() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)

fun Float.px2dip() = this / screenDensity + 0.5f

fun Float.px2sp() = this / Resources.getSystem().displayMetrics.scaledDensity

fun getStatusBarHeight() = Resources.getSystem().let {
    val resourceId = it.getIdentifier("status_bar_height", "dimen", "android")
    it.getDimensionPixelSize(resourceId)
}

fun getNavigationBarHeight(): Int = Resources.getSystem().let {
    val resourceId = it.getIdentifier("navigation_bar_height", "dimen", "android")
    it.getDimensionPixelSize(resourceId)
}

fun AppCompatActivity.replaceActionBar(toolbar: Toolbar) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(true)
        setDisplayShowTitleEnabled(true)
    }
}

fun Activity.getApplicationFrameSize(): IntArray {
    val frame = Rect()
    window.decorView.getWindowVisibleDisplayFrame(frame)
    return intArrayOf(frame.width(), frame.height())
}

fun Activity.isNavigationBarShown(): Boolean {
    val remainHeight = getRealSize()[1] - getStatusBarHeight()
    return getApplicationFrameSize()[1] != remainHeight
}

fun Activity.getRealSize(): IntArray {
    val point = Point()
    windowManager.defaultDisplay.getRealSize(point)
    return intArrayOf(point.x, point.y)
}

fun Context.getActionBarSize(): Int {
    var actionBarSize = 0
    val typedValue = TypedValue()
    if (theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
        val ta = obtainStyledAttributes(
            typedValue.resourceId,
            intArrayOf(android.R.attr.actionBarSize)
        )
        actionBarSize = ta.getDimensionPixelSize(0, 0)
        ta.recycle()
    }
    return actionBarSize
}

/**
 * 为 status bar 设置颜色
 */
fun Activity.setColorForStatusBar(
    @ColorInt color: Int,
    @IntRange(from = 0, to = 255) statusBarAlpha: Int = DEFAULT_STATUS_BAR_ALPHA
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window?.let {
            it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            it.statusBarColor = calculateStatusColor(color, statusBarAlpha)
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val decorView = window.decorView as ViewGroup
        val fakeStatusBar = decorView.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
        fakeStatusBar.let {
            (it != null).yes {
                if (it.visibility == View.GONE) it.visibility = View.VISIBLE
                it.setBackgroundColor(calculateStatusColor(color, statusBarAlpha))
            }.otherwise {
                decorView.addView(createStatusBarView(color, statusBarAlpha))
            }
        }
        setRootView()
    }
}

/**
 * 设置状态栏文字为深色
 */
@TargetApi(Build.VERSION_CODES.M)
fun Activity.setStatusBarLightMode() {
    setMiUiStatusBarDarkMode()
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}

/**
 * 设置状态栏文字为浅色
 */
@TargetApi(Build.VERSION_CODES.M)
fun Activity.setStatusBarDarkMode() {
    setMiUiStatusBarDarkMode(true)
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}

/**
 * 适配 MiUi V6 以上状态栏
 */
@SuppressLint("PrivateApi")
private fun Activity.setMiUiStatusBarDarkMode(darkMode: Boolean = false) {
    val clazz = window.javaClass

    try {
        val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        val darkModeFlag = field.getInt(layoutParams)
        val extraFlagField = clazz.getMethod(
            "setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType
        )
        extraFlagField.invoke(window, darkMode.yes { darkModeFlag }.otherwise { 0 }, darkModeFlag)
    } catch (e: Exception) {
        Log.e("StatusBarUtils", "MiUiDarkMode", e)
    }
}

/**
 * 创建一个虚拟的 status bar
 */
private fun Activity.createStatusBarView(
    @ColorInt color: Int,
    @IntRange(from = 0, to = 255) alpha: Int = 0
) = View(this).apply {
    layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight()
    )
    setBackgroundColor(calculateStatusColor(color, alpha))
    id = FAKE_STATUS_BAR_VIEW_ID
}

/**
 * 设置根布局参数
 */
private fun Activity.setRootView() =
    findViewById<ViewGroup>(android.R.id.content)
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
) = (alpha == 0).yes { color }.otherwise {
    val a = 1 - alpha / 255f
    val red = color shr 16 and 0xff
    val green = color shr 8 and 0xff
    val blue = color and 0xff
    (0xff shr 24) or (((red * a + 0.5).toInt()) shr 16) or (((green * a + 0.5).toInt()) shr 8) or ((blue * a + 0.5).toInt())
}