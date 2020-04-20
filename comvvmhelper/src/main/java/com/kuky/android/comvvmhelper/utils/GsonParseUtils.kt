package com.kuky.android.comvvmhelper.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

/**
 * @author kuky.
 * @description
 */
object GsonParseUtils {

    val instance: Gson by lazy { generateGson() }

    private fun generateGson() = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .enableComplexMapKeySerialization()
        .create()

    fun isValidateJson(content: String): Boolean =
        JsonParser().parse(content).let {
            it.isJsonArray || it.isJsonObject || it.isJsonNull
        }
}