package com.kuky.android.comvvmhelper.listener

/**
 * @author kuky.
 * @description
 */

typealias PermissionGranted = () -> Unit

typealias PermissionDenied = (MutableList<String>, MutableList<String>) -> Unit