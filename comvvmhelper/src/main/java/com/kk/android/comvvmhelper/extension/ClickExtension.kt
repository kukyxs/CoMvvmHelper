package com.kk.android.comvvmhelper.extension

import android.view.View
import com.kk.android.comvvmhelper.R

/**
 * @author kuky.
 * @description debounced click
 * @reference https://github.com/Blankj/AndroidUtilCode
 */
internal var DEFAULT_DEBOUNCE_TIME = 300L

/**
 * @param isGlobal is global worked
 */
fun View.setOnDebounceClickListener(
    duration: Long = DEFAULT_DEBOUNCE_TIME,
    debounceCall: (View?) -> Unit
) {
    setOnClickListener(object : OnDebounceClickListener(duration) {
        override fun onDebounceClick(view: View?) {
            debounceCall(view)
        }
    })
}

abstract class OnDebounceClickListener(
    private val duration: Long = DEFAULT_DEBOUNCE_TIME
) : View.OnClickListener {

    private var mIsEnabled = true
    private val mEnabledAgain = Runnable { mIsEnabled = true }

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
        if (mIsEnabled) {
            mIsEnabled = false
            v?.postDelayed(mEnabledAgain, duration)
            onDebounceClick(v)
        }
    }

    abstract fun onDebounceClick(view: View?)
}