package com.kk.android.comvvmhelper.utils

import android.os.Build

/**
 * @author kuky.
 * @description
 */
fun actionsByR(belowR: () -> Unit, aboveR: () -> Unit) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) belowR() else aboveR()
}