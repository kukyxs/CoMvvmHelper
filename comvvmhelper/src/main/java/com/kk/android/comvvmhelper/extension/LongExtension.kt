package com.kk.android.comvvmhelper.extension

import java.text.SimpleDateFormat
import java.util.Locale

fun Long.formatFileSize(): String {
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var size = toDouble()
    var unit = 0

    while (size >= 1024 && unit < units.size - 1) {
        size /= 1024
        unit++
    }

    return String.format("%.2f %s", size, units[unit])
}

fun Long.formatTimestamp(format: String = "yyyy-MM-dd"): String =
    SimpleDateFormat(format, Locale.getDefault()).format(this)