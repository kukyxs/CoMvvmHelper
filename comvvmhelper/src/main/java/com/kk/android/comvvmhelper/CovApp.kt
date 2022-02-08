package com.kk.android.comvvmhelper

import android.app.Application
import com.kk.android.comvvmhelper.abs.AbsImageEngine
import com.kk.android.comvvmhelper.abs.ImageLoadHelper
import com.kk.android.comvvmhelper.extension.DEFAULT_DEBOUNCE_TIME
import com.kk.android.comvvmhelper.helper.HttpSingle
import com.kk.android.comvvmhelper.helper.RequestConfig
import com.kk.android.comvvmhelper.helper.RetrofitHelper
import com.kk.android.comvvmhelper.helper.isDebugMode
import com.tencent.mmkv.MMKV
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter

/**
 * @author kuky.
 * @description Initial Necessary params
 */
fun Application.startCov(covApp: CovApp.() -> Unit) {
    val covConfig = CovApp().apply(covApp)

    MMKV.initialize(this)

    isDebugMode = covConfig.openDebug

    DEFAULT_DEBOUNCE_TIME = covConfig.debounceTime

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
    var debounceTime: Long = 300,
    var koinPropertiesFile: String? = "",
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