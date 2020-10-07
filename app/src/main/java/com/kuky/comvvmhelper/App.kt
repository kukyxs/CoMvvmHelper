package com.kuky.comvvmhelper

import android.app.Application
import com.kk.android.comvvmhelper.startCov
import org.koin.dsl.module

/**
 * @author kuky.
 * @description
 */
class App : Application() {
    private val viewModelModule = module { }

    override fun onCreate() {
        super.onCreate()

        startCov {
            koinModules = mutableListOf(viewModelModule) // your koin modules
            baseUrl = "https://www.google.com" // your retrofit base url if use
//            koinPropertiesFile = "" // koin properties file
//            client = OkHttpClient.Builder().build() // OkHttp or Retrofit client
//            customRetrofitCallAdapterArray = mutableListOf() // your retrofit call adapters if use
//            customRetrofitConverterFactoryArray = mutableListOf() // your retrofit converter factories if use
        }

//        start cov can be replaced by

        /* koinInit {
            koinPropertiesFile = ""
            koinModules = mutableListOf(viewModelModule)
        }

        globalHttpClient {
            baseUrl = "https://www.google.com"
            client = OkHttpClient.Builder().build()
            customCallAdapter = mutableListOf()
            customConvertAdapter = mutableListOf()
        } */
    }
}

