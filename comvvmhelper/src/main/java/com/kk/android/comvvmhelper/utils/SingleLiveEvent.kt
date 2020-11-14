package com.kk.android.comvvmhelper.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author kuky.
 * @description single observer for LiveData or use [UnPeekLiveData]
 */
class SingleLiveEvent<T> : LiveData<T> {
    private val mPending = AtomicBoolean(false)

    constructor() : super()

    constructor(value: T) : super(value)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(SingleLiveEvent::class.java.simpleName, "Multiple observers registered but only one will be notified of changes.")
        }

        super.observe(owner, {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(value: T?) {
        mPending.set(true)
        super.setValue(value)
    }

    public override fun postValue(value: T) {
        super.postValue(value)
    }

    @MainThread
    fun clear() {
        value = null
    }
}
