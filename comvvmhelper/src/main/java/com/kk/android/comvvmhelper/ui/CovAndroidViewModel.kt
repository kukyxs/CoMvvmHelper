package com.kk.android.comvvmhelper.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel

/**
 * @author kuky.
 * @description
 */
@Deprecated("Will remove at future version")
open class CovAndroidViewModel(application: Application) : AndroidViewModel(application) {

    protected val mContext: Context = application.applicationContext

    private val mPool: CovLiveDataPool = CovLiveDataPool()

    fun <T> getLiveDataEvent(event: String) = mPool.getLiveEvent<T>(event)

    fun <T> getSingleLiveEvent(event: String) = mPool.getSingleEvent<T>(event)

    fun <T> getUnPeekEvent(event: String, allowNull: Boolean = false) = mPool.getUnPeekEvent<T>(event, allowNull)

    override fun onCleared() {
        super.onCleared()
        mPool.clear()
    }
}