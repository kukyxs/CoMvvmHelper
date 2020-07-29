package com.kk.android.comvvmhelper.extension

import android.view.View
import com.kk.android.comvvmhelper.R

/**
 * @author kuky.
 * @description
 */
private const val DEFAULT_DEBOUNCE_TIME = 200L

fun View.onDebounceClickListener(
    isGlobal: Boolean = false,
    duration: Long = DEFAULT_DEBOUNCE_TIME,
    debounceCall: (View?) -> Unit
) {
    setOnClickListener(object : OnDebounceClickListener(isGlobal, duration) {
        override fun onDebounceClick(view: View?) {
            debounceCall(view)
        }
    })
}

abstract class OnDebounceClickListener(
    private val isGlobal: Boolean = false, private val duration: Long = DEFAULT_DEBOUNCE_TIME
) : View.OnClickListener {

    private var mEnabled = true
    private val mEnableAgain = Runnable { mEnabled = true }

    private fun isInvalidate(view: View?, duration: Long): Boolean {
        if (view == null) return false

        val curTime = System.currentTimeMillis()
        val tag = view.getTag(R.id.debounce_tag)
        if (tag !is Long) {
            view.setTag(R.id.debounce_tag, curTime)
            return true
        }

        if (curTime - tag < 0) {
            view.setTag(R.id.debounce_tag, curTime)
            return false
        } else if (curTime - tag < duration) {
            return false
        }

        view.setTag(R.id.debounce_tag, curTime)
        return true
    }

    override fun onClick(v: View?) {
        if (isGlobal) {
            if (mEnabled) {
                mEnabled = false
                v?.postDelayed(mEnableAgain, duration)
                onDebounceClick(v)
            }
        } else {
            if (isInvalidate(v, duration)) onDebounceClick(v)
        }
    }

    abstract fun onDebounceClick(view: View?)
}