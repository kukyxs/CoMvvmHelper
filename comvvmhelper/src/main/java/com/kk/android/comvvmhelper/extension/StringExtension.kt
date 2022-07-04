package com.kk.android.comvvmhelper.extension

import android.os.Build
import android.text.Html
import android.text.Spanned

/**
 * @author kuky.
 * @description
 */

@Suppress("DEPRECATION")
fun String.renderHtml(flags: Int? = null): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, flags ?: Html.FROM_HTML_MODE_LEGACY)
} else Html.fromHtml(this)