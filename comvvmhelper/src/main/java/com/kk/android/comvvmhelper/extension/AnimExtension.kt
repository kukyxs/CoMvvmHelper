package com.kk.android.comvvmhelper.extension

import android.view.animation.Animation

/**
 * @author kuky.
 * @description
 */
inline fun Animation.doOnAnimStart(crossinline action: (animation: Animation?) -> Unit) =
    addAnimationListener(onAnimStart = action)

inline fun Animation.doOnAnimEnd(crossinline action: (animation: Animation?) -> Unit) =
    addAnimationListener(onAnimEnd = action)

inline fun Animation.doOnAnimRepeat(crossinline action: (animation: Animation?) -> Unit) =
    addAnimationListener(onAnimRepeat = action)

inline fun Animation.addAnimationListener(
    crossinline onAnimStart: (animation: Animation?) -> Unit = {},
    crossinline onAnimEnd: (animation: Animation?) -> Unit = {},
    crossinline onAnimRepeat: (animation: Animation?) -> Unit = {}
): Animation.AnimationListener {
    val listener = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            onAnimStart(animation)
        }

        override fun onAnimationEnd(animation: Animation?) {
            onAnimEnd(animation)
        }

        override fun onAnimationRepeat(animation: Animation?) {
            onAnimRepeat(animation)
        }
    }

    setAnimationListener(listener)
    return listener
}