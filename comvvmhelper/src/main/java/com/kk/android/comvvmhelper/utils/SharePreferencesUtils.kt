package com.kk.android.comvvmhelper.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * @author kuky.
 * @description
 */
private fun Context.defaultSharePreferences(name: String? = null): SharedPreferences =
    getSharedPreferences(name ?: "${packageName}_share_preference", Context.MODE_PRIVATE)

fun Context.registerShareChangeListener(shareChange: SharedPreferences.OnSharedPreferenceChangeListener) =
    defaultSharePreferences().registerOnSharedPreferenceChangeListener(shareChange)

fun Context.unregisterShareChangeListener(shareChange: SharedPreferences.OnSharedPreferenceChangeListener) =
    defaultSharePreferences().unregisterOnSharedPreferenceChangeListener(shareChange)

fun Context.saveString(key: String, value: String) =
    defaultSharePreferences().edit().putString(key, value).apply()

fun Context.getString(key: String, default: String = ""): String =
    defaultSharePreferences().getString(key, default) ?: default

fun Context.saveInteger(key: String, value: Int) =
    defaultSharePreferences().edit().putInt(key, value).apply()

fun Context.getInteger(key: String, default: Int = 0): Int =
    defaultSharePreferences().getInt(key, default)

fun Context.saveLong(key: String, value: Long) =
    defaultSharePreferences().edit().putLong(key, value).apply()

fun Context.getLong(key: String, default: Long = 0L): Long =
    defaultSharePreferences().getLong(key, default)

fun Context.saveFloat(key: String, value: Float) =
    defaultSharePreferences().edit().putFloat(key, value).apply()

fun Context.getFloat(key: String, default: Float = 0F): Float =
    defaultSharePreferences().getFloat(key, default)

fun Context.saveBoolean(key: String, value: Boolean) =
    defaultSharePreferences().edit().putBoolean(key, value).apply()

fun Context.getBoolean(key: String, default: Boolean = false): Boolean =
    defaultSharePreferences().getBoolean(key, default)

fun Context.saveStringSet(key: String, value: MutableSet<String>?) =
    defaultSharePreferences().edit().putStringSet(key, value).apply()

fun Context.getStringSet(key: String): MutableSet<String> =
    defaultSharePreferences().getStringSet(key, mutableSetOf()) ?: mutableSetOf()