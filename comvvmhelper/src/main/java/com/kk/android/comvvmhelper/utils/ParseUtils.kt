@file:Suppress("MemberVisibilityCanBePrivate")

package com.kk.android.comvvmhelper.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.kk.android.comvvmhelper.helper.SingletonHelperArg0
import java.lang.reflect.Type


/**
 * @author kuky.
 * @description
 */
class ParseUtils {

    companion object : SingletonHelperArg0<ParseUtils>(::ParseUtils)

    private val gson: Gson = generateGson()

    fun <T> parseToJson(value: T): String = gson.toJson(value)

    fun <T> parseFromJson(json: String, typeOfT: Type): T = gson.fromJson(json, typeOfT)

    fun <T> parseFromJson(json: String, classOfT: Class<T>): T = gson.fromJson(json, classOfT)

    private fun generateGson() = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .registerTypeAdapter(String::class.java, NonnullStringAdapter())
        .enableComplexMapKeySerialization()
        .create()

    fun isValidateJson(content: String): Boolean =
        (content.startsWith("{") && content.endsWith("}")) || (content.startsWith("[") && content.endsWith("]"))
}

class NonnullStringAdapter : TypeAdapter<String>() {
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