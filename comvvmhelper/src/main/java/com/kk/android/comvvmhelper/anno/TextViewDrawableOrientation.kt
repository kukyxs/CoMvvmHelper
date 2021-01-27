package com.kk.android.comvvmhelper.anno

import androidx.annotation.IntDef

/**
 * @author kuky.
 * @description
 */
@IntDef(value = [TextViewDrawableOrientation.START, TextViewDrawableOrientation.TOP, TextViewDrawableOrientation.END, TextViewDrawableOrientation.BOTTOM])
@Retention(AnnotationRetention.SOURCE)
annotation class TextViewDrawableOrientation {
    companion object {
        const val START = 0
        const val TOP = 1
        const val END = 2
        const val BOTTOM = 3
    }
}