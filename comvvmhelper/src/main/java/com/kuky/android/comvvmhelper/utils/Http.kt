package com.kuky.android.comvvmhelper.utils

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author kuky.
 * @description
 */
@ExperimentalCoroutinesApi
suspend fun http(init: OkRequestWrapper.() -> Unit) {
    val wrapper = OkRequestWrapper().apply(init)

    check(wrapper.baseUrl.matches(Regex("(http|https)?://(\\S)+"))) { "Illegal url" }

    executeForResult(wrapper)
}

data class OkRequestWrapper(
    var flowDispatcher: CoroutineDispatcher? = null,
    var baseUrl: String = "",
    var method: String = "get",
    var requestBody: RequestBody? = null,
    var params: HashMap<String, Any> = hashMapOf(),
    var onSuccess: suspend (Response) -> Unit = {},
    var onFail: suspend (Throwable) -> Unit = {}
)

@Suppress("BlockingMethodInNonBlockingContext")
@ExperimentalCoroutinesApi
private suspend fun executeForResult(wrapper: OkRequestWrapper) {
    flow { emit(onExecute(wrapper)) }
        .flowOn(wrapper.flowDispatcher ?: GlobalScope.coroutineContext)
        .catch { wrapper.onFail(it) }
        .collect { wrapper.onSuccess(it) }
}

private fun onExecute(wrapper: OkRequestWrapper): Response {
    val request = when (wrapper.method.toLowerCase(Locale.getDefault())) {
        "post" -> Request.Builder().url(wrapper.baseUrl)
            .post(
                if (wrapper.requestBody == null) generateRequestBody(wrapper.params)
                else wrapper.requestBody!!
            ).build()

        "put" -> Request.Builder().url(wrapper.baseUrl)
            .put(
                if (wrapper.requestBody == null) generateRequestBody(wrapper.params)
                else wrapper.requestBody!!
            ).build()

        "delete" -> Request.Builder().url(wrapper.baseUrl)
            .delete(
                if (wrapper.requestBody == null) generateRequestBody(wrapper.params)
                else wrapper.requestBody!!
            ).build()

        else -> Request.Builder().url(generateGetUrl(wrapper.params, wrapper.baseUrl)).get().build()
    }

    return generateOkHttpClient().newCall(request).execute()
}

private fun generateGetUrl(params: HashMap<String, Any>, url: String) =
    if (url.contains(Regex("[?=]"))) url
    else {
        val urlSb = StringBuilder(url).append("?")
        if (params.isNotEmpty()) params.forEach { entry ->
            urlSb.append(entry.key).append("=").append(Gson().toJson(entry.value))
        }
        urlSb.toString()
    }

private fun generateRequestBody(params: HashMap<String, Any>) =
    FormBody.Builder().apply {
        if (params.isNotEmpty()) params.forEach { entry ->
            add(entry.key, Gson().toJson(entry.value))
        }
    }.build()

private fun generateOkHttpClient() = OkHttpClient.Builder()
    .connectTimeout(5_000L, TimeUnit.MILLISECONDS)
    .readTimeout(10_000, TimeUnit.MILLISECONDS)
    .writeTimeout(30_000, TimeUnit.MILLISECONDS)
    .addInterceptor(HttpLoggingInterceptor(
        object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (message.startsWith("[") || message.startsWith("{")) logJson(message)
                else logInfo(message)
            }
        }
    ).apply { level = HttpLoggingInterceptor.Level.BODY }).build()