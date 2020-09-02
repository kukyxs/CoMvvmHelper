package com.kk.android.comvvmhelper.helper

/**
 * @author kuky.
 * @description quick create singleton with single argument, T is your class type and AR is your argument type
 */
open class SingletonHelperArg0<T : Any>(creator: () -> T) {
    private var sCreator: (() -> T)? = creator

    @Volatile
    private var sInstance: T? = null

    fun instance(): T {
        return sInstance ?: synchronized(this) {
            sInstance ?: sCreator!!().also { sInstance = it; sCreator = null }
        }
    }
}

open class SingletonHelperArg1<T : Any, AR>(creator: (AR) -> T) {
    private var sCreator: ((AR) -> T)? = creator

    @Volatile
    private var sInstance: T? = null

    fun instance(arg: AR): T {
        return sInstance ?: synchronized(this) {
            sInstance ?: sCreator!!(arg).also { sInstance = it; sCreator = null }
        }
    }
}

open class SingletonHelperArg2<T : Any, AR1, AR2>(creator: (AR1, AR2) -> T) {
    private var sCreator: ((AR1, AR2) -> T)? = creator

    @Volatile
    private var sInstance: T? = null

    fun instance(arg1: AR1, arg2: AR2): T {
        return sInstance ?: synchronized(this) {
            sInstance ?: sCreator!!(arg1, arg2).also { sInstance = it; sCreator = null }
        }
    }
}

open class SingletonHelperArg3<T : Any, AR1, AR2, AR3>(creator: (AR1, AR2, AR3) -> T) {
    private var sCreator: ((AR1, AR2, AR3) -> T)? = creator

    @Volatile
    private var sInstance: T? = null

    fun instance(arg1: AR1, arg2: AR2, arg3: AR3): T {
        return sInstance ?: synchronized(this) {
            sInstance ?: sCreator!!(arg1, arg2, arg3).also { sInstance = it; sCreator = null }
        }
    }
}