package com.kk.android.comvvmhelper.extension

import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar

/**
 * @author kuky.
 * @description
 */
inline fun AppCompatSeekBar.doOnProgressChange(crossinline action: (SeekBar?, Int, Boolean) -> Unit) =
    addSeekChangeListener(onProgressChange = action)

inline fun AppCompatSeekBar.doOnStartTrackingTouch(crossinline action: (SeekBar?) -> Unit) =
    addSeekChangeListener(onStartTrackingTouch = action)

inline fun AppCompatSeekBar.doOnStopTrackingTouch(crossinline action: (SeekBar?) -> Unit) =
    addSeekChangeListener(onStopTrackingTouch = action)

inline fun AppCompatSeekBar.addSeekChangeListener(
    crossinline onProgressChange: (SeekBar?, Int, Boolean) -> Unit = { _, _, _ -> },
    crossinline onStartTrackingTouch: (SeekBar?) -> Unit = {},
    crossinline onStopTrackingTouch: (SeekBar?) -> Unit = {}
): SeekBar.OnSeekBarChangeListener {
    val seekChange = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onProgressChange(seekBar, progress, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            onStartTrackingTouch(seekBar)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            onStopTrackingTouch(seekBar)
        }
    }
    setOnSeekBarChangeListener(seekChange)
    return seekChange
}