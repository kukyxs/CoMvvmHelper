package com.kk.android.comvvmhelper.extension

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.TextView
import com.kk.android.comvvmhelper.anno.TextViewDrawableOrientation

/**
 * @author kuky.
 * @description
 */

fun TextView.drawableStart(drawableRes: Int?, size: Int?, drawablePadding: Int?) =
    appendDrawable(drawableRes, size, drawablePadding, TextViewDrawableOrientation.START)

fun TextView.drawableStart(path: String?, size: Int?, drawablePadding: Int?) =
    appendDrawable(path, size, drawablePadding, TextViewDrawableOrientation.START)

fun TextView.drawableTop(drawableRes: Int?, size: Int?, drawablePadding: Int?) =
    appendDrawable(drawableRes, size, drawablePadding, TextViewDrawableOrientation.TOP)

fun TextView.drawableTop(path: String?, size: Int?, drawablePadding: Int?) =
    appendDrawable(path, size, drawablePadding, TextViewDrawableOrientation.TOP)

fun TextView.drawableEnd(drawableRes: Int?, size: Int?, drawablePadding: Int?) =
    appendDrawable(drawableRes, size, drawablePadding, TextViewDrawableOrientation.END)

fun TextView.drawableEnd(path: String?, size: Int?, drawablePadding: Int?) =
    appendDrawable(path, size, drawablePadding, TextViewDrawableOrientation.END)

fun TextView.drawableBottom(drawableRes: Int?, size: Int?, drawablePadding: Int?) =
    appendDrawable(drawableRes, size, drawablePadding, TextViewDrawableOrientation.BOTTOM)

fun TextView.drawableBottom(path: String?, size: Int?, drawablePadding: Int?) =
    appendDrawable(path, size, drawablePadding, TextViewDrawableOrientation.BOTTOM)

///////////////////////////////////////////
// Set Drawable For TextView /////////////
/////////////////////////////////////////
internal fun TextView.appendDrawable(
    drawableRes: Int?, size: Int? = null,
    drawablePadding: Int? = null,
    orientation: Int = TextViewDrawableOrientation.START
) {
    if (drawablePadding != null) compoundDrawablePadding = drawablePadding

    val tarCompoundDrawables = compoundDrawables

    tarCompoundDrawables[orientation] =
        (drawableRes == null).yes { null }.otherwise {
            context.drawableValue(drawableRes!!).apply {
                setBounds(0, 0, size ?: intrinsicWidth, size ?: intrinsicHeight)
            }
        }

    setCompoundDrawables(tarCompoundDrawables[0], tarCompoundDrawables[1], tarCompoundDrawables[2], tarCompoundDrawables[3])
}

internal fun TextView.appendDrawable(
    path: String? = null, size: Int? = null,
    drawablePadding: Int? = null,
    orientation: Int = TextViewDrawableOrientation.START
) {
    if (drawablePadding != null) compoundDrawablePadding = drawablePadding

    val tarCompoundDrawables = compoundDrawables

    tarCompoundDrawables[orientation] =
        path.isNullOrBlank().yes { null }.otherwise {
            BitmapDrawable(context.resources, BitmapFactory.decodeFile(path)).apply {
                setBounds(0, 0, size ?: intrinsicWidth, size ?: intrinsicHeight)
            }
        }

    setCompoundDrawables(tarCompoundDrawables[0], tarCompoundDrawables[1], tarCompoundDrawables[2], tarCompoundDrawables[3])
}