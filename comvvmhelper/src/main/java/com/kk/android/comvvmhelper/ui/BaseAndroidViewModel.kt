package com.kk.android.comvvmhelper.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver

/**
 * @author kuky.
 * @description
 */
open class BaseAndroidViewModel(
    application: Application
) : AndroidViewModel(application), LifecycleObserver {

    protected val context = application.applicationContext

    private val mPool: LiveDataPool = LiveDataPool()

    fun <T> getLiveDataEvent(event: String) = mPool.getLiveEvent<T>(event)

    fun <T> getSingleLiveEvent(event: String) = mPool.getSingleEvent<T>(event)

    override fun onCleared() {
        super.onCleared()
        mPool.clear()
    }
}