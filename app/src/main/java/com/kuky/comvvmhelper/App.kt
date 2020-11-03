package com.kuky.comvvmhelper

import android.app.Application
import com.kk.android.comvvmhelper.startCov
import com.kuky.comvvmhelper.di.adapterModule
import com.kuky.comvvmhelper.di.viewModelModule
import com.kuky.comvvmhelper.helper.GlideEngine
import org.koin.dsl.module

/**
 * @author kuky.
 * @description
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startCov {
            loadEngine = GlideEngine() // image load engine for ImageViewBinding, if not use ignore this param

            koinModules = mutableListOf(viewModelModule, adapterModule) // your koin modules

            baseUrl = "https://www.wanandroid.com/" // your retrofit base url if use

//            koinPropertiesFile = "" // koin properties file
//            client = OkHttpClient.Builder().build() // OkHttp or Retrofit client
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
        */
    }
}

