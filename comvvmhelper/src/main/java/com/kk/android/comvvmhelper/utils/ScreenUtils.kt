@file:Suppress("MemberVisibilityCanBePrivate")

package com.kk.android.comvvmhelper.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.TypedValue

/**
 * @author kuky.
 * @description
 */

@Deprecated(message = "replaced by Context.screenWidth", replaceWith = ReplaceWith("Context.screenWidth"))
val screenWidth = Resources.getSystem().displayMetrics.widthPixels

@Deprecated(message = "replaced by Context.screenHeight", replaceWith = ReplaceWith("Context.screenHeight"))
val screenHeight = Resources.getSystem().displayMetrics.heightPixels

val screenDensity = Resources.getSystem().displayMetrics.density

val Context.screenWidth: Int get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int get() = resources.displayMetrics.heightPixels

val statusBarHeight: Int
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    get() = Resources.getSystem().let {
        it.getDimensionPixelOffset(it.getIdentifier("status_bar_height", "dimen", "android"))
    }

val navigationBarHeight: Int
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    get() = Resources.getSystem().let {
        it.getDimensionPixelOffset(it.getIdentifier("navigation_bar_height", "dimen", "android"))
    }

fun Float.dp2px() = screenDensity * this + 0.5f

fun Float.sp2px() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)

fun Float.px2dp() = this / screenDensity + 0.5f

fun Float.px2sp() = this / Resources.getSystem().displayMetrics.scaledDensity

fun Activity.isNavigationBarShown(): Boolean {
    val (_, realHeight) = getRealSize()
    val (_, frameHeight) = getApplicationFrameSize()
    return frameHeight != (realHeight - statusBarHeight)
}

fun Activity.getApplicationFrameSize(): IntArray {
    val frame = Rect()
    window.decorView.getWindowVisibleDisplayFrame(frame)
    return intArrayOf(frame.width(), frame.height())
}

@Suppress("DEPRECATION")
fun Activity.getRealSize(): IntArray {
    val point = Point()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        windowManager.defaultDisplay.getRealSize(point)
    } else {
        display?.getRealSize(point)
    }
    return intArrayOf(point.x, point.y)
}

fun Context.getActionBarSize(): Int {
    var actionBarSize = 0
    val typedValue = TypedValue()
    if (theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
        val ta = obtainStyledAttributes(typedValue.resourceId, intArrayOf(android.R.attr.actionBarSize))
        actionBarSize = ta.getDimensionPixelSize(0, 0)
        ta.recycle()
    }
    return actionBarSize
}