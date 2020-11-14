package com.kk.android.comvvmhelper.utils

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


/**
 * @author kuky.
 * @description 防止 LiveData 数据倒灌
 * @reference https://github.com/KunMinX/UnPeek-LiveData
 */
open class ProtectedUnPeekLiveData<T> : LiveData<T>() {
    protected var isAllowNullValue = false
    private val observers = hashMapOf<Int, Boolean>()

    fun observeInActivity(activity: AppCompatActivity, observer: Observer<in T>) {
        val storeId = System.identityHashCode(activity.viewModelStore)
        observe(storeId, activity, observer)
    }

    fun observeInFragment(fragment: Fragment, observer: Observer<in T>) {
        val storeId = System.identityHashCode(fragment.viewModelStore)
        observe(storeId, fragment, observer)
    }

    private fun observe(storeId: Int, owner: LifecycleOwner, observer: Observer<in T>) {
        if (observers[storeId] == null) {
            observers[storeId] = true
        }

        super.observe(owner, {
            if (observers[storeId] == false) {
                observers[storeId] = true
                if (it != null || isAllowNullValue) {
                    observer.onChanged(it)
                }
            }
        })
    }

    override fun setValue(value: T?) {
        if (value != null || isAllowNullValue) {
            observers.entries.forEach { it.setValue(false) }
            super.setValue(value)
        }
    }

    protected fun clear() {
        value = null
    }
}

class UnPeekLiveData<T>(allowNullValue: Boolean = false) : ProtectedUnPeekLiveData<T>() {
    init {
        isAllowNullValue = allowNullValue
    }

    override fun setValue(value: T?) {
        super.setValue(value)
        Log.i(UnPeekLiveData::class.java.simpleName, "set value: $value")
    }

    override fun postValue(value: T?) {
        super.postValue(value)
        Log.i(UnPeekLiveData::class.java.simpleName, "post value: $value")
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        throw IllegalArgumentException("use observeInActivity or observeInFragment instead")
    }

    override fun observeForever(observer: Observer<in T>) {
        throw IllegalArgumentException("do not call observeForever at UnPeekLivaData")
    }
}