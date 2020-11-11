package com.kk.android.comvvmhelper.utils

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * @author kuky.
 * @description
 */
fun Context.defaultDataStore(): DataStore<Preferences> =
    createDataStore(name = "${packageName}_data_store")

/**
 * only support Int, Long, Boolean, Float, Double, String
 */
inline fun <reified T : Any> Context.fetchDataStoreData(
    keyName: String, noinline default: (() -> T?)? = null
): Flow<T?> = defaultDataStore().data.catch {
    emit(emptyPreferences())
}.map { pref ->
    pref[preferencesKey(keyName)] ?: default?.invoke()
}

suspend inline fun <reified T : Any> Context.saveToDataStore(keyName: String, value: T) =
    defaultDataStore().edit { store ->
        store[preferencesKey<T>(keyName)] = value
    }

/**
 * T only support Int, Long, Boolean, Float, Double, String
 */
inline fun <reified T : Any, reified R : Any> Context.fetchTransDataFromDataStore(
    keyName: String, noinline trans: (T?) -> R?, noinline default: (() -> R?)? = null
): Flow<R?> = defaultDataStore().data.catch {
    emit(emptyPreferences())
}.map { pref ->
    trans.invoke(pref[preferencesKey(keyName)]) ?: default?.invoke()
}

suspend inline fun <reified T : Any> Context.saveTransToDataStore(
    keyName: String, value: T, noinline trans: (T?) -> String = { Gson().toJson(it) }
) {
    defaultDataStore().edit { store ->
        store[preferencesKey<String>(keyName)] = trans(value)
    }
}