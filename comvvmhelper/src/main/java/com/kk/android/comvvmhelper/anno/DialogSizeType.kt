package com.kk.android.comvvmhelper.anno

import androidx.annotation.IntDef

/**
 * @author kuky.
 * @description
 */
@IntDef(value = [DialogSizeType.FILL_SIZE, DialogSizeType.WRAP_SIZE])
@Retention(AnnotationRetention.SOURCE)
annotation class DialogSizeType {
    companion object {
        const val FILL_SIZE = 0
        const val WRAP_SIZE = 1
    }
}
