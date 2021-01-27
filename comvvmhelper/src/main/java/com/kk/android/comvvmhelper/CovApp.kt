package com.kk.android.comvvmhelper

import android.app.Application
import com.kk.android.comvvmhelper.abs.AbsImageEngine
import com.kk.android.comvvmhelper.abs.ImageLoadHelper
import com.kk.android.comvvmhelper.helper.HttpSingle
import com.kk.android.comvvmhelper.helper.RequestConfig
import com.kk.android.comvvmhelper.helper.RetrofitHelper
import com.kk.android.comvvmhelper.helper.isDebugMode
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

    isDebugMode = covConfig.openDebug

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

    globalLoadEngine(covConfig.loadEngine)
}

data class CovApp(
    var openDebug: Boolean = true,
    var koinPropertiesFile: String? = "",
    var koinModules: MutableList<Module> = mutableListOf(),
    var baseUrl: String = "",
    var client: OkHttpClient? = null,
    var customRetrofitCallAdapterArray: MutableList<CallAdapter.Factory> = mutableListOf(),
    var customRetrofitConverterFactoryArray: MutableList<Converter.Factory> = mutableListOf(),
    var loadEngine: AbsImageEngine? = null
)

///////////////////////////////
// Image Load Engine /////////
/////////////////////////////
fun globalLoadEngine(engine: AbsImageEngine?) {
    ImageLoadHelper.instance().engine = engine
}

////////////////////////////////
// Request Initial DSL ////////
//////////////////////////////
fun globalHttpClient(init: RequestConfig.() -> Unit) {
    val rqConfig = RequestConfig().apply(init)
    HttpSingle.instance().globalHttpClient(rqConfig.client)
    RetrofitHelper.instance().run {
        if (rqConfig.baseUrl.isNotBlank()) {
            setBaseUrl(rqConfig.baseUrl)
        }
        setClient(rqConfig.client)
        setCustomCallAdapter(rqConfig.customCallAdapter)
        setCustomConvertFactory(rqConfig.customConvertAdapter)
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
