package com.kk.android.comvvmhelper.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel

/**
 * @author kuky.
 * @description
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {

    private val mPool: LiveDataPool = LiveDataPool()

    fun <T> getLiveDataEvent(event: String) = mPool.getLiveEvent<T>(event)

    fun <T> getSingleLiveEvent(event: String) = mPool.getSingleEvent<T>(event)

    override fun onCleared() {
        super.onCleared()
        mPool.clear()
    }
}