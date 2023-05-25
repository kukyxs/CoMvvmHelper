package com.kuky.comvvmhelper

import android.app.Application
import android.util.Log
import com.kk.android.comvvmhelper.helper.BasicDecryptInterceptor
import com.kk.android.comvvmhelper.helper.DynamicUrlInterceptor
import com.kk.android.comvvmhelper.startCov
import com.kuky.comvvmhelper.di.adapterModule
import com.kuky.comvvmhelper.helper.GlideEngine
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author kuky.
 * @description
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startCov {
            openDebug = BuildConfig.DEBUG

            loadEngine = GlideEngine() // image load engine for ImageViewBinding, if not use ignore this param

            koinModules = mutableListOf(adapterModule) // your koin modules

            baseUrl = Constant.WAN_URL // your retrofit base url if use

//            koinPropertiesFile = "" // koin properties file
            client = OkHttpClient.Builder()
                .addInterceptor(DynamicUrlInterceptor())
                .addInterceptor(object : BasicDecryptInterceptor() {
                    override fun String.decrypt() = """
                        <font color="red">[decryption]</font>
                        $this
                        <font color="red">[decryption]</font>
                    """.trimIndent()
                })
                .addInterceptor(HttpLoggingInterceptor { message ->
                    Log.i("HttpRequest", message)
                }).build()

//            customRetrofitCallAdapterArray = mutableListOf() // your retrofit call adapters if use
//            customRetrofitConverterFactoryArray = mutableListOf() // your retrofit converter factories if use
        }

        /* or replaced by

        globalLoadEngine(GlideEngine()) // register image load engine

        koinInit {                      // register koin
            koinPropertiesFile = ""
            koinModules = mutableListOf(viewModelModule)
        }

        globalHttpClient {              // register network
            baseUrl = "https://www.google.com"
            client = OkHttpClient.Builder().build()
            customCallAdapter = mutableListOf()
            customConvertAdapter = mutableListOf()
        }

        MMKV.initialize(this)
        */
    }
}

