package com.kk.android.comvvmhelper.extension

import android.content.Context
import android.content.DialogInterface
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import android.widget.TextView
import com.kk.android.comvvmhelper.anko.AlertBuilder
import com.kk.android.comvvmhelper.anko.alert

fun View.hideSoftInput() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showSoftInputForce() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

fun Context.scrollTextAlert(
    content: SpannableStringBuilder,
    init: AlertBuilder<DialogInterface>.() -> Unit
) = alert {
    apply(init)
    customView = ScrollView(this@scrollTextAlert).apply {
        setPadding(80, 60, 80, 0)
        overScrollMode = View.OVER_SCROLL_NEVER
        addView(TextView(this@scrollTextAlert).apply {
            textSize = 16f
            setTextColor(colorValue(android.R.color.black))
            text = content
            movementMethod = LinkMovementMethod()
        })
    }
}