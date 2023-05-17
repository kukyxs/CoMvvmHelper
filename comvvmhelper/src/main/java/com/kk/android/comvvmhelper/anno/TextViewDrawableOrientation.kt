package com.kk.android.comvvmhelper.anno

/**
 * @author kuky.
 * @description
 */
sealed class TextViewDrawableOrientation(val value: Int) {
    object START : TextViewDrawableOrientation(0)
    object TOP : TextViewDrawableOrientation(1)
    object END : TextViewDrawableOrientation(2)
    object BOTTOM : TextViewDrawableOrientation(3)
}