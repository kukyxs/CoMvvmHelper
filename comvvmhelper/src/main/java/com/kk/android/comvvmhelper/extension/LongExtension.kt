package com.kk.android.comvvmhelper.extension

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

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

fun Long.formatDuration(hourSeparator: String = ":", minSeparator: String = ":", secondSeparator: String = ""): String {
    val hours = TimeUnit.MILLISECONDS.toHours(this)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(hours)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours)
    return if (hours > 0) String.format("%02d$hourSeparator%02d$minSeparator%02d:$secondSeparator", hours, minutes, seconds)
    else String.format("%02d$minSeparator%02d$secondSeparator", minutes, seconds)
}