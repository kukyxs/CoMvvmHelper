package com.kk.android.comvvmhelper.extension

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.TextView

/**
 * @author kuky.
 * @description
 */

fun TextView.drawableStart(drawableRes: Int?, size: Int?, drawablePadding: Int?) =
    appendDrawable(drawableRes, size, drawablePadding, TextViewDrawableOrientation.Start)

fun TextView.drawableStart(path: String?, size: Int?, drawablePadding: Int?) =
    appendDrawable(path, size, drawablePadding, TextViewDrawableOrientation.Start)

fun TextView.drawableTop(drawableRes: Int?, size: Int?, drawablePadding: Int?) =
    appendDrawable(drawableRes, size, drawablePadding, TextViewDrawableOrientation.Top)

fun TextView.drawableTop(path: String?, size: Int?, drawablePadding: Int?) =
    appendDrawable(path, size, drawablePadding, TextViewDrawableOrientation.Top)

fun TextView.drawableEnd(drawableRes: Int?, size: Int?, drawablePadding: Int?) =
    appendDrawable(drawableRes, size, drawablePadding, TextViewDrawableOrientation.End)

fun TextView.drawableEnd(path: String?, size: Int?, drawablePadding: Int?) =
    appendDrawable(path, size, drawablePadding, TextViewDrawableOrientation.End)

fun TextView.drawableBottom(drawableRes: Int?, size: Int?, drawablePadding: Int?) =
    appendDrawable(drawableRes, size, drawablePadding, TextViewDrawableOrientation.Bottom)

fun TextView.drawableBottom(path: String?, size: Int?, drawablePadding: Int?) =
    appendDrawable(path, size, drawablePadding, TextViewDrawableOrientation.Bottom)

///////////////////////////////////////////
// Set Drawable For TextView /////////////
/////////////////////////////////////////
internal enum class TextViewDrawableOrientation(val code: Int) {
    Start(0), Top(1), End(2), Bottom(3)
}

internal fun TextView.appendDrawable(
    drawableRes: Int?, size: Int? = null,
    drawablePadding: Int? = null,
    orientation: TextViewDrawableOrientation = TextViewDrawableOrientation.Start
) {
    if (drawablePadding != null) compoundDrawablePadding = drawablePadding

    val tarCompoundDrawables = compoundDrawables

    tarCompoundDrawables[orientation.code] = (drawableRes == null).yes { null }
        .otherwise {
            context.drawableValue(drawableRes!!).apply {
                setBounds(0, 0, size ?: intrinsicWidth, size ?: intrinsicHeight)
            }
        }

    setCompoundDrawables(tarCompoundDrawables[0], tarCompoundDrawables[1], tarCompoundDrawables[2], tarCompoundDrawables[3])
}

internal fun TextView.appendDrawable(
    path: String? = null, size: Int? = null,
    drawablePadding: Int? = null,
    orientation: TextViewDrawableOrientation = TextViewDrawableOrientation.Start
) {
    if (drawablePadding != null) compoundDrawablePadding = drawablePadding

    val tarCompoundDrawables = compoundDrawables

    tarCompoundDrawables[orientation.code] = path.isNullOrBlank().yes { null }
        .otherwise {
            BitmapDrawable(context.resources, BitmapFactory.decodeFile(path)).apply {
                setBounds(0, 0, size ?: intrinsicWidth, size ?: intrinsicHeight)
            }
        }

    setCompoundDrawables(tarCompoundDrawables[0], tarCompoundDrawables[1], tarCompoundDrawables[2], tarCompoundDrawables[3])
}