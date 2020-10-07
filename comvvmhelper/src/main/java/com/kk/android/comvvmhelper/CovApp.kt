package com.kk.android.comvvmhelper

import android.app.Application
import com.kk.android.comvvmhelper.helper.HttpSingle
import com.kk.android.comvvmhelper.helper.RequestConfig
import com.kk.android.comvvmhelper.helper.RetrofitHelper
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import retrofit2.CallAdapter
import retrofit2.Converter

/**
 * @author kuky.
 * @description Initial Necessary params
 */
fun Application.startCov(covApp: CovApp.() -> Unit) {
    val covConfig = CovApp().apply(covApp)

    koinInit {
        koinPropertiesFile = covConfig.koinPropertiesFile
        koinModules = covConfig.koinModules
    }

    globalHttpClient {
        baseUrl = covConfig.baseUrl
        client = covConfig.client
        customCallAdapter = covConfig.customRetrofitCallAdapterArray
        customConvertAdapter = covConfig.customRetrofitConverterFactoryArray
    }
}

data class CovApp(
    var koinPropertiesFile: String? = "",
    var koinModules: MutableList<Module> = mutableListOf(),
    var baseUrl: String = "",
    var client: OkHttpClient? = null,
    var customRetrofitCallAdapterArray: MutableList<CallAdapter.Factory> = mutableListOf(),
    var customRetrofitConverterFactoryArray: MutableList<Converter.Factory> = mutableListOf()
)

////////////////////////////////
// Request Initial DSL ////////
//////////////////////////////
fun globalHttpClient(init: RequestConfig.() -> Unit) {
    val rqConfig = RequestConfig().apply(init)
    HttpSingle.instance().globalHttpClient(rqConfig.client)
    RetrofitHelper.instance().run {
        mBaseUrl = rqConfig.baseUrl
        mClient = rqConfig.client
        mCustomCallAdapterList = rqConfig.customCallAdapter
        mCustomConverterFactoryList = rqConfig.customConvertAdapter
    }
}

/////////////////////////////
// Koin Initial DSL ////////
///////////////////////////
fun Application.koinInit(propertiesFile: String, vararg modules: Module) {
    koinInit {
        koinPropertiesFile = propertiesFile
        koinModules = modules.toMutableList()
    }
}

fun Application.koinInit(vararg modules: Module) {
    koinInit { koinModules = modules.toMutableList() }
}

fun Application.koinInit(
    creator: ModuleCreator.() -> Unit
) {
    val kaConfig = ModuleCreator().apply(creator)

    startKoin {
        androidLogger(Level.ERROR) // if level is not ERROR, app will be crashed
        androidContext(this@koinInit)
        androidFileProperties(kaConfig.koinPropertiesFile ?: "koin.properties")
        modules(kaConfig.koinModules)
    }
}

data class ModuleCreator(
    var koinPropertiesFile: String? = "",
    var koinModules: MutableList<Module> = mutableListOf()
)
