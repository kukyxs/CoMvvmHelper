package com.kuky.comvvmhelper.helper

import com.kk.android.comvvmhelper.helper.DynamicUrlInterceptor
import com.kuky.comvvmhelper.Constant
import com.kuky.comvvmhelper.entity.BaseResultData
import com.kuky.comvvmhelper.entity.Tops
import com.kuky.comvvmhelper.entity.WebsiteData
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * @author kuky.
 * @description
 */
interface ApiService {

    @Headers("${DynamicUrlInterceptor.URL_HEADER_TAG}:${Constant.WAN_URL}")
    @GET("friend/json")
    suspend fun requestRepositoryInfo(): BaseResultData<MutableList<WebsiteData>>

    @Headers("${DynamicUrlInterceptor.URL_HEADER_TAG}:${Constant.JH_URL}")
    @GET("/toutiao/index")
    suspend fun requestTop(@Query("key") key: String): Tops
}