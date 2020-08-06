package com.kk.android.comvvmhelper.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * @author kuky.
 * @description
 */
object ParseUtils {

    val instance by lazy<Gson> { generateGson() }

    private fun generateGson() = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .enableComplexMapKeySerialization()
        .create()

    fun isValidateJson(content: String): Boolean =
        (content.startsWith("{") && content.endsWith("}")) || (content.startsWith("[") && content.endsWith("]"))
}