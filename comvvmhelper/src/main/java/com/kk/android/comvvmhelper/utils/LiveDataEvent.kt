package com.kk.android.comvvmhelper.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * handle livedata data sticky
 */
typealias MutableWrappedLiveData<T> = MutableLiveData<LiveDataWrapEvent<T>>

typealias WrappedLiveData<T> = LiveData<LiveDataWrapEvent<T>>

/////////////////////////////////////
/////////////////////////////////////
fun <T> wrappedMutableLivedata(init: T) = MutableLiveData(LiveDataWrapEvent(init))

fun <T> MutableWrappedLiveData<T>.postEventValue(value: T) {
    postValue(LiveDataWrapEvent(value))
}

fun <T> MutableWrappedLiveData<T>.toWrappedLiveData() = this as WrappedLiveData<T>

inline fun <T> WrappedLiveData<T>.observerEvent(
    owner: LifecycleOwner,
    crossinline onChange: (data: T) -> Unit
) {
    observe(owner) { t ->
        t.getContentIfNotHandled()?.let(onChange)
    }
}

inline fun <T> MutableWrappedLiveData<T>.observerEvent(
    owner: LifecycleOwner,
    crossinline onChange: (data: T) -> Unit
) {
    observe(owner) { t ->
        t.getContentIfNotHandled()?.let(onChange)
    }
}

/////////////////////////////////////
/////////////////////////////////////
open class LiveDataWrapEvent<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? = if (hasBeenHandled) null else {
        hasBeenHandled = true; content
    }

    fun peekContent(): T = content
}