@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.kk.android.comvvmhelper.helper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kk.android.comvvmhelper.utils.ParseUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.coroutines.resumeWithException

/**
 * @author kuky.
 * @description DSL for http request
 */

class Http {
    val wrapper = OkRequestWrapper()

    fun url(url: String): Http {
        wrapper.baseUrl = url
        return this
    }

    fun body(body: RequestBody): Http {
        wrapper.requestBody = body
        return this
    }

    fun params(params: HashMap<String, Any>): Http {
        wrapper.params = params
        return this
    }

    fun headers(headers: HashMap<String, String>): Http {
        wrapper.headers = headers
        return this
    }

    fun catch(block: (Throwable) -> Unit): Http {
        wrapper.onFail = { block(it) }
        return this
    }

    fun onResponse(block: (Response) -> Unit): Http {
        wrapper.onSuccess = { block(it) }
        return this
    }

    inline fun <reified T> onResult(noinline block: (T?) -> Unit): Http {
        wrapper.onSuccess = { block(it.checkResult()) }
        return this
    }

    inline fun <reified T> onListResult(noinline block: (MutableList<T>) -> Unit): Http {
        wrapper.onSuccess = { block(it.checkList()) }
        return this
    }

    suspend fun get() = re("get")

    suspend fun post() = re("post")

    suspend fun put() = re("put")

    suspend fun delete() = re("delete")

    private suspend fun re(method: String) {
        wrapper.method = method
        check(wrapper.baseUrl.matches(urlRegex)) { "Illegal url" }
        HttpSingle.instance().executeForResult(wrapper)
    }

    /**
     * request with result
     */
    suspend inline fun <reified T> getResponse(): T? = response("get")?.checkResult()

    suspend inline fun <reified T> getListResponse(): MutableList<T> = response("get")?.checkList() ?: mutableListOf()

    suspend inline fun <reified T> postResponse(): T? = response("post")?.checkResult()

    suspend inline fun <reified T> postListResponse(): MutableList<T> = response("post")?.checkList() ?: mutableListOf()

    suspend inline fun <reified T> putResponse(): T? = response("put")?.checkResult()

    suspend inline fun <reified T> putListResponse(): MutableList<T> = response("put")?.checkList() ?: mutableListOf()

    suspend inline fun <reified T> deleteResponse(): T? = response("delete")?.checkResult()

    suspend inline fun <reified T> deleteListResponse(): MutableList<T> = response("delete")?.checkList() ?: mutableListOf()

    suspend fun response(method: String): Response? {
        wrapper.method = method
        check(wrapper.baseUrl.matches(urlRegex)) { "Illegal url" }
        try {
            return HttpSingle.instance().enqueueForResult(wrapper)
        } catch (e: Exception) {
            wrapper.onFail(e)
        }
        return null
    }
}

/**
 * dsl fot request
 */
suspend fun http(init: OkRequestWrapper.() -> Unit) {
    val wrapper = OkRequestWrapper().apply(init)

    check(wrapper.baseUrl.matches(urlRegex)) { "Illegal url" }

    HttpSingle.instance().executeForResult(wrapper)
}

inline fun <reified T> Response.checkResult(): T? {
    val response = this.body?.string() ?: ""
    return try {
        if (response.isBlank()) null
        else ParseUtils.instance().parseFromJson(response, T::class.java)
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T> Response.checkList(): MutableList<T> {
    val response = this.body?.string() ?: ""
    return try {
        if (response.isBlank()) mutableListOf()
        else ParseUtils.instance().parseFromJson(
            response, object : TypeToken<MutableList<T>>() {}.type
        ) ?: mutableListOf()
    } catch (e: Exception) {
        mutableListOf()
    }
}

fun Response.checkText(): String = this.body?.string() ?: ""

data class OkRequestWrapper(
    var baseUrl: String = "",
    var method: String = "get",
    var requestBody: RequestBody? = null,
    var params: HashMap<String, Any> = hashMapOf(),
    var headers: HashMap<String, String> = hashMapOf(),
    var onSuccess: suspend (Response) -> Unit = {},
    var onFail: suspend (Throwable) -> Unit = {}
)

/**
 * Request singleton
 */
class HttpSingle private constructor() : KLogger {

    companion object : SingletonHelperArg0<HttpSingle>(::HttpSingle)

    private var mOkHttpClient: OkHttpClient? = null

    fun globalHttpClient(client: OkHttpClient?) {
        mOkHttpClient = client
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun executeForResult(wrapper: OkRequestWrapper) {
        flow { emit(onExecute(wrapper)) }
            .catch { wrapper.onFail(it) }
            .collectLatest { wrapper.onSuccess(it) }
    }

    suspend fun enqueueForResult(wrapper: OkRequestWrapper) =
        suspendCancellableCoroutine<Response> { continuation ->
            val request = requestBuild(wrapper)

            val call = (mOkHttpClient ?: generateOkHttpClient()).also { mOkHttpClient = it }.newCall(request)

            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    continuation.resumeWith(Result.success(response))
                }
            })

            continuation.invokeOnCancellation { call.cancel() }
        }


    private fun onExecute(wrapper: OkRequestWrapper): Response {
        val request = requestBuild(wrapper)

        return (mOkHttpClient ?: generateOkHttpClient().also { mOkHttpClient = it })
            .newCall(request).execute()
    }

    private fun requestBuild(wrapper: OkRequestWrapper): Request {
        val requestBuilder = Request.Builder()
        if (wrapper.headers.isNotEmpty()) {
            wrapper.headers.forEach { entry ->
                requestBuilder.addHeader(entry.key, entry.value)
            }
        }

        val request = when (wrapper.method.lowercase(Locale.getDefault())) {
            "post" -> requestBuilder.url(wrapper.baseUrl).post(
                wrapper.requestBody ?: generateRequestBody(wrapper.params)
            ).build()

            "put" -> requestBuilder.url(wrapper.baseUrl).put(
                wrapper.requestBody ?: generateRequestBody(wrapper.params)
            ).build()

            "delete" -> requestBuilder.url(wrapper.baseUrl).delete(
                wrapper.requestBody ?: generateRequestBody(wrapper.params)
            ).build()

            else -> requestBuilder.url(generateGetUrl(wrapper.params, wrapper.baseUrl))
                .get().build()
        }
        return request
    }

    //region generate http params
    private fun generateGetUrl(params: HashMap<String, Any>, url: String) =
        if (url.contains("?")) url
        else {
            val urlSb = StringBuilder(url).append("?")
            if (params.isNotEmpty()) {
                params.forEach { entry ->
                    val value = entry.value
                    urlSb.append(entry.key).append("=")
                        .append(if (value is String) value else Gson().toJson(value))
                        .append("&")
                }
            }
            urlSb.substring(0, urlSb.length - 1).toString()
        }

    private fun generateRequestBody(params: HashMap<String, Any>) =
        FormBody.Builder().apply {
            if (params.isNotEmpty()) params.forEach { entry ->
                val value = entry.value
                add(
                    entry.key,
                    if (value is String) value else ParseUtils.instance().parseToJson(value)
                )
            }
        }.build()
    //endregion
}