@file:Suppress("MemberVisibilityCanBePrivate")

package com.kuky.comvvmhelper

import com.kk.android.comvvmhelper.utils.LogUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * @author kuky.
 * @description
 */
object RetrofitHelper {

    val retrofit: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .client(generateOkHttpClient())
            .build()
    }

    private fun generateOkHttpClient() = OkHttpClient.Builder()
        .connectTimeout(5_000L, TimeUnit.MILLISECONDS)
        .readTimeout(20_000, TimeUnit.MILLISECONDS)
        .writeTimeout(30_000, TimeUnit.MILLISECONDS)
        .addInterceptor(HttpLoggingInterceptor(
            object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    if (message.startsWith("[") || message.startsWith("{"))
                        LogUtils.logJson(message)
                    else LogUtils.logInfo(message)
                }
            }
        ).apply { level = HttpLoggingInterceptor.Level.BODY }).build()
}