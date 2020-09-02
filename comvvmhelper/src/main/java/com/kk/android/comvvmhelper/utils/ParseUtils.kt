@file:Suppress("MemberVisibilityCanBePrivate")

package com.kk.android.comvvmhelper.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.kk.android.comvvmhelper.helper.SingletonHelperArg0

/**
 * @author kuky.
 * @description
 */
class ParseUtils {

    companion object : SingletonHelperArg0<ParseUtils>(::ParseUtils)

    val gson: Gson

    init {
        gson = generateGson()
    }

    private fun generateGson() = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .registerTypeAdapter(String::class.java, sStringAdapter)
        .enableComplexMapKeySerialization()
        .create()

    fun isValidateJson(content: String): Boolean =
        (content.startsWith("{") && content.endsWith("}")) || (content.startsWith("[") && content.endsWith("]"))

    private val sStringAdapter = object : TypeAdapter<String>() {
        override fun write(writer: JsonWriter?, value: String?) {
            try {
                if (value == null) {
                    writer?.nullValue()
                    return
                }
                writer?.value(value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun read(reader: JsonReader?): String {
            try {
                if (reader?.peek() == JsonToken.NULL) {
                    reader.nextNull()
                    return ""
                }
                return reader?.nextString() ?: ""
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }
    }
}