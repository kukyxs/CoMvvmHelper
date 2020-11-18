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

    private var mBaseUrl = ""
    private var mClient: OkHttpClient? = null
    private var mCustomCallAdapterList: MutableList<CallAdapter.Factory> = mutableListOf()
    private var mCustomConverterFactoryList: MutableList<Converter.Factory> = mutableListOf()

    fun setBaseUrl(url: String) {
        check(url.matches(urlRegex)) { "Illegal url: $url" }
        mBaseUrl = url
    }

    fun setClient(client: OkHttpClient?) {
        mClient = client
    }

    fun setCustomCallAdapter(vararg adapter: CallAdapter.Factory) {
        mCustomCallAdapterList = adapter.toMutableList()
    }

    fun setCustomCallAdapter(callAdapters: MutableList<CallAdapter.Factory>) {
        mCustomCallAdapterList = callAdapters
    }

    fun setCustomConvertFactory(vararg factory: Converter.Factory) {
        mCustomConverterFactoryList = factory.toMutableList()
    }

    fun setCustomConvertFactory(factories: MutableList<Converter.Factory>) {
        mCustomConverterFactoryList = factories
    }

    /**
     * default support gson converter
     */
    fun retrofitProvider(): Retrofit {
        check(mBaseUrl.matches(urlRegex)) { "Illegal url: $mBaseUrl" }

        return retrofit ?: synchronized(this) {
            retrofit ?: Retrofit.Builder()
                .baseUrl(mBaseUrl).apply {
                    mCustomCallAdapterList.forEach { addCallAdapterFactory(it) }
                    mCustomConverterFactoryList.filterNot { it is GsonConverterFactory }.forEach { addConverterFactory(it) }
                }.addConverterFactory(GsonConverterFactory.create())
                .client(mClient ?: generateOkHttpClient()).build()
        }
    }

    inline fun <reified T> createApiService(): T = retrofitProvider().create(T::class.java)
}

///////////////////////////////////
// Create Retrofit ApiService ////
/////////////////////////////////
inline fun <reified T> createService(): T {
    return RetrofitHelper.instance().createApiService()
}

/////////////////////////////////
// DSL For Retrofit Params /////
///////////////////////////////
data class RequestConfig(
    var baseUrl: String = "",
    var client: OkHttpClient? = null,
    var customCallAdapter: MutableList<CallAdapter.Factory> = mutableListOf(),
    var customConvertAdapter: MutableList<Converter.Factory> = mutableListOf()
)