@file:Suppress("MemberVisibilityCanBePrivate")

package com.kuky.comvvmhelper

import com.kuky.android.comvvmhelper.helper.generateOkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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
}