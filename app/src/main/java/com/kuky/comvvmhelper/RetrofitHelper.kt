@file:Suppress("MemberVisibilityCanBePrivate")

package com.kuky.comvvmhelper

import com.kk.android.comvvmhelper.helper.KLogger
import com.kk.android.comvvmhelper.helper.SingletonHelperArg1
import com.kk.android.comvvmhelper.helper.jsonPrint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * @author kuky.
 * @description
 */
class RetrofitHelper private constructor(baseUrl: String) : KLogger {

    companion object : SingletonHelperArg1<RetrofitHelper, String>(::RetrofitHelper)

    val retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(generateOkHttpClient())
            .build()
    }

    private fun generateOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5_000L, TimeUnit.MILLISECONDS)
            .readTimeout(20_000, TimeUnit.MILLISECONDS)
            .writeTimeout(30_000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor(
                object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        jsonPrint { message }
                    }
                }
            ).apply { level = HttpLoggingInterceptor.Level.BODY }).build()
    }
}