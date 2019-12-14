package com.kuky.android.comvvmhelper.permission

import java.util.concurrent.atomic.AtomicInteger

/**
 * @author kuky.
 * @description
 */
internal object PermissionMap {
    private val atomicInteger = AtomicInteger(0xFF)
    private val map = mutableMapOf<Int, PermissionCallback>()

    fun put(callback: PermissionCallback): Int =
        atomicInteger.getAndIncrement().apply {
            map[this] = callback
        }

    fun fetch(requestCode: Int): PermissionCallback? =
        map[requestCode].apply {
            map.remove(requestCode)
        }
}