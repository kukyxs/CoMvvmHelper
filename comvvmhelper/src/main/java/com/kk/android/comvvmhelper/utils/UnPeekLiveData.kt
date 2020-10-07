package com.kk.android.comvvmhelper.utils

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes
import java.util.*
import kotlin.concurrent.timerTask

/**
 * @author kuky.
 * @description 防止 LiveData 数据倒灌
 * @reference https://github.com/KunMinX/UnPeek-LiveData
 */
open class ProtectedUnPeekLiveData<T> : LiveData<T>() {
    private var isCleaning = false
    private var hasHandled = true
    private var isDelaying = false

    private val mTimer = Timer()
    private var mTimerTask: TimerTask? = null

    protected var delayToCleanEvent = 1000L
    protected var isAllowNullValue = false
    protected var isAllowToClean = true

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, {
            isCleaning.yes {
                hasHandled = true; isDelaying = false; isCleaning = false
            }.otherwise {
                hasHandled.yes { observer.onChanged(it) }
                    .otherwise { hasHandled = true; isDelaying = true; observer.onChanged(it) }
            }
        })
    }

    override fun observeForever(observer: Observer<in T>) {
        throw IllegalArgumentException("Do not use observeForever for communication between pages to avoid lifecycle security issues")
    }

    override fun setValue(value: T) {
        if (!isCleaning && (!isAllowNullValue && value == null)) {
            return
        }

        hasHandled = false
        isDelaying = false
        super.setValue(value)

        mTimerTask?.let { it.cancel(); mTimer.purge() }

        value?.let { _ ->
            val task = timerTask { clear() }.also { mTimerTask = it }
            mTimer.schedule(task, delayToCleanEvent)
        }
    }

    private fun clear() {
        isAllowToClean.yes { isCleaning = true; super.postValue(null) }
            .otherwise { hasHandled = true; isDelaying = false }
    }
}

class UnPeekLiveData<T> : ProtectedUnPeekLiveData<T>() {
    companion object {
        fun <T> create() = UnPeekLiveDataBuilder<T>().create()
    }

    override fun setValue(value: T) {
        super.setValue(value)
        Log.i(UnPeekLiveData::class.java.simpleName, "setValue")
    }

    override fun postValue(value: T) {
        super.postValue(value)
        Log.i(UnPeekLiveData::class.java.simpleName, "postValue")
    }

    data class UnPeekLiveDataBuilder<T>(
        val eventSurvivalTime: Long = 1000,
        val allowNullValue: Boolean = false,
        val allowToClean: Boolean = true
    ) {
        fun create(): UnPeekLiveData<T> = UnPeekLiveData<T>().apply {
            delayToCleanEvent = eventSurvivalTime
            isAllowNullValue = allowNullValue
            isAllowToClean = allowToClean
        }
    }
}