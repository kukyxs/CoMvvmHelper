package com.kuky.android.comvvmhelper.extension

import android.view.View

/**
 * @author kuky.
 * @description
 */

abstract class SingleClickListener(private val interval: Long = 1000) : View.OnClickListener {
    private var lastTime = 0L

    override fun onClick(v: View?) {
        val current = System.currentTimeMillis()

        if (current - lastTime >= interval) {
            lastTime = current
            onSingleClick(v)
        }
    }

    abstract fun onSingleClick(v: View?)
}

fun View.onSingleClick(time: Long = 1000, action: () -> Unit) {
    setOnClickListener(object : SingleClickListener(time) {
        override fun onSingleClick(v: View?) {
            action.invoke()
        }
    })
}