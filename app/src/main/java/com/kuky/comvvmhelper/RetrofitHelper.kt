@file:Suppress("MemberVisibilityCanBePrivate")

package com.kuky.comvvmhelper

import com.kk.android.comvvmhelper.helper.jsonPrint
import com.kk.android.comvvmhelper.helper.kLogger
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

    private fun generateOkHttpClient(): OkHttpClient {
        val logger = kLogger<RetrofitHelper>()

        return OkHttpClient.Builder()
            .connectTimeout(5_000L, TimeUnit.MILLISECONDS)
            .readTimeout(20_000, TimeUnit.MILLISECONDS)
            .writeTimeout(30_000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor(
                object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        logger.jsonPrint { message }
                    }
                }
            ).apply { level = HttpLoggingInterceptor.Level.BODY }).build()
    }
}