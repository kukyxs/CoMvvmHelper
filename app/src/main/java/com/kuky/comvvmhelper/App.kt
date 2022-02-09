package com.kuky.comvvmhelper

import android.app.Application
import com.kk.android.comvvmhelper.startCov
import com.kuky.comvvmhelper.helper.GlideEngine
import dagger.hilt.android.HiltAndroidApp

/**
 * @author kuky.
 * @description
 */

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startCov {
            loadEngine = GlideEngine() // image load engine for ImageViewBinding, if not use ignore this param

            baseUrl = Constant.WAN_URL // your retrofit base url if use

//            client = OkHttpClient.Builder().build() // OkHttp or Retrofit client
//            customRetrofitCallAdapterArray = mutableListOf() // your retrofit call adapters if use
//            customRetrofitConverterFactoryArray = mutableListOf() // your retrofit converter factories if use
        }

        /* or replaced by

        globalLoadEngine(GlideEngine()) // register image load engine

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

