package com.kk.android.comvvmhelper.helper

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * @author kuky.
 * @description
 */
internal val urlRegex = Regex("(http|https)?://(\\S)+")

internal fun generateOkHttpClient() = OkHttpClient.Builder()
    .connectTimeout(5_000L, TimeUnit.MILLISECONDS)
    .readTimeout(20_000, TimeUnit.MILLISECONDS)
    .writeTimeout(30_000, TimeUnit.MILLISECONDS)
    .addInterceptor(DynamicUrlInterceptor())
    .addInterceptor(HttpLoggingInterceptor { message ->
        Log.i("HttpRequest", message)
    }.apply { level = HttpLoggingInterceptor.Level.BODY }).build()

/**
 * 解密拦截器
 */
abstract class BasicDecryptInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        if (response.isSuccessful) response.body?.let { body ->
            val source = body.source()
            val buffer = source.buffer
            source.request(Long.MAX_VALUE)

            var charset = charset()
            val contentType = body.contentType()?.also { charset = it.charset(charset) ?: Charset.defaultCharset() }
            val bodyString = buffer.clone().readString(charset)
            val responseData = bodyString.decrypt()
            val newBody = responseData.toResponseBody(contentType)
            response = response.newBuilder().body(newBody).build()
        }
        return response
    }

    abstract fun String.decrypt(): String

    protected open fun charset(): Charset = Charset.defaultCharset()
}

///////////////////////////////////////////////////////
// Dynamic to change retrofit base url ///////////////
/////////////////////////////////////////////////////
class DynamicUrlInterceptor : Interceptor {
    companion object {
        const val URL_HEADER_TAG = "BaseUrl"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val orgRequest = chain.request()
        val oldUrl = orgRequest.url
        val requestBuilder = orgRequest.newBuilder()
        val headers = orgRequest.headers[URL_HEADER_TAG]

        if (!headers.isNullOrBlank()) {
            requestBuilder.removeHeader(URL_HEADER_TAG)
            val newUrlCont = headers.toString()
            if (newUrlCont.matches(urlRegex)) {
                newUrlCont.toHttpUrlOrNull()?.let {
                    if (oldUrl notSame it) {
                        val newHttpUrl = oldUrl.newBuilder()
                            .scheme(it.scheme).host(it.host)
                            .port(it.port).build()
                        return chain.proceed(requestBuilder.url(newHttpUrl).build())
                    }
                }
            }
        }
        return chain.proceed(orgRequest)
    }

    private infix fun HttpUrl.notSame(httpUrl: HttpUrl): Boolean {
        return this.scheme != httpUrl.scheme || this.host != httpUrl.host || this.port != httpUrl.port
    }
}
