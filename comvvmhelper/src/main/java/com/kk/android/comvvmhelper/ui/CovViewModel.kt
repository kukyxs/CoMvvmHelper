package com.kk.android.comvvmhelper.ui

import androidx.lifecycle.ViewModel

/**
 * @author kuky.
 * @description
 */
@Deprecated("Will remove at future version")
open class CovViewModel : ViewModel() {

    private val mPool: CovLiveDataPool = CovLiveDataPool()

    fun <T> getLiveDataEvent(event: String) = mPool.getLiveEvent<T>(event)

    fun <T> getSingleLiveEvent(event: String) = mPool.getSingleEvent<T>(event)

    fun <T> getUnPeekEvent(event: String, allowNull: Boolean = false) = mPool.getUnPeekEvent<T>(event, allowNull)

    override fun onCleared() {
        super.onCleared()
        mPool.clear()
    }
}