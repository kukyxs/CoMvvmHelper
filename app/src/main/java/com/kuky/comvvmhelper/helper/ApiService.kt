package com.kuky.comvvmhelper.helper

import com.kuky.comvvmhelper.entity.BaseResultData
import com.kuky.comvvmhelper.entity.WebsiteData
import retrofit2.http.GET

/**
 * @author kuky.
 * @description
 */
interface ApiService {

    @GET("friend/json")
    suspend fun requestRepositoryInfo(): BaseResultData<MutableList<WebsiteData>>
}