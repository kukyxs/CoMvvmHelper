package com.kuky.comvvmhelper.entity

/**
 * @author kuky.
 * @description
 */

data class BaseResultData<T>(
    val `data`: T,
    val errorCode: Int,
    val errorMsg: String
)

data class WebsiteData(
    val icon: String,
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
)

/////////////////////////////////
// juhe news tops //////////////
/////////////////////////////////
data class Tops(
    val error_code: Int,
    val reason: String,
    val result: Result
)

data class Result(
    val `data`: List<Data>,
    val stat: String
)

data class Data(
    val author_name: String,
    val category: String,
    val date: String,
    val thumbnail_pic_s: String,
    val thumbnail_pic_s02: String,
    val thumbnail_pic_s03: String,
    val title: String,
    val uniquekey: String,
    val url: String
)