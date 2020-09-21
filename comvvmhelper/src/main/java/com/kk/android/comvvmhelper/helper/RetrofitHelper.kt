package com.kk.android.comvvmhelper.helper

import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author kuky.
 * @description
 */
class RetrofitHelper private constructor() {
    companion object : SingletonHelperArg0<RetrofitHelper>(::RetrofitHelper)

    @Volatile
    private var retrofit: Retrofit? = null

    internal var mBaseUrl = ""

    internal var mClient: OkHttpClient? = null

    internal var mCustomCallAdapterList: MutableList<CallAdapter.Factory> = mutableListOf()

    internal var mCustomConverterFactoryList: MutableList<Converter.Factory> = mutableListOf()

    fun setBaseUrl(url: String) {
        check(url.matches(Regex("(http|https)?://(\\S)+"))) { "Illegal url: $url" }
        mBaseUrl = url
    }

    fun setClient(client: OkHttpClient?) {
        mClient = client
    }

    fun setCustomCallAdapter(vararg adapter: CallAdapter.Factory) {
        mCustomCallAdapterList = adapter.toMutableList()
    }

    fun setCustomConvertFactory(vararg factory: Converter.Factory) {
        mCustomConverterFactoryList = factory.toMutableList()
    }

    fun retrofitProvider(): Retrofit {
        check(mBaseUrl.matches(Regex("(http|https)?://(\\S)+"))) { "Illegal url: $mBaseUrl" }

        return retrofit ?: synchronized(this) {
            retrofit ?: Retrofit.Builder()
                .baseUrl(mBaseUrl).apply {
                    mCustomCallAdapterList.forEach { addCallAdapterFactory(it) }
                    mCustomConverterFactoryList.filterNot { it is GsonConverterFactory }.forEach { addConverterFactory(it) }
                }.addConverterFactory(GsonConverterFactory.create())
                .client(mClient ?: generateOkHttpClient()).build()
        }
    }
}

///////////////////////////////////
// Create Retrofit ApiService ////
/////////////////////////////////
inline fun <reified T> createService(): T {
    return RetrofitHelper.instance().retrofitProvider().create(T::class.java)
}

data class RequestConfig(
    var baseUrl: String = "",
    var client: OkHttpClient? = null,
    var customCallAdapter: MutableList<CallAdapter.Factory> = mutableListOf(),
    var customConvertAdapter: MutableList<Converter.Factory> = mutableListOf()
)