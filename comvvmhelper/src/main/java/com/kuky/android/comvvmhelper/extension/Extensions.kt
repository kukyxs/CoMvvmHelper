@file:Suppress("DEPRECATION")

package com.kuky.android.comvvmhelper.extension

import android.os.Build
import android.text.Html
import androidx.core.text.HtmlCompat

/**
 * @author kuky.
 * @description
 */
fun String.renderHtml(): String =
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        Html.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
    else Html.fromHtml(this).toString()