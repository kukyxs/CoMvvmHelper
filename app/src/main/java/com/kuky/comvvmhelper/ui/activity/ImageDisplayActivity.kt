package com.kuky.comvvmhelper.ui.activity

import android.os.Bundle
import com.google.gson.Gson
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.anno.StatusBarTextColorMode
import com.kk.android.comvvmhelper.extension.safeLaunch
import com.kk.android.comvvmhelper.helper.ePrint
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kk.android.comvvmhelper.utils.fetchDataStoreData
import com.kk.android.comvvmhelper.utils.fetchTransDataFromDataStore
import com.kk.android.comvvmhelper.utils.saveToDataStore
import com.kk.android.comvvmhelper.utils.saveTransToDataStore
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.ActivityImageDisplayBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@ActivityConfig(statusBarColorString = "#AAAAAA", statusBarTextColorMode = StatusBarTextColorMode.Light)
class ImageDisplayActivity : BaseActivity<ActivityImageDisplayBinding>() {

    data class User(val name: String)

    override fun layoutId() = R.layout.activity_image_display

    override fun initActivity(savedInstanceState: Bundle?) {
        safeLaunch {
            block = {
                saveToDataStore("username", "kuky")
                saveTransToDataStore("user", User("kuky"), { Gson().toJson(it) })

                delay(1000)

                fetchDataStoreData<String>("username")
                fetchTransDataFromDataStore<String, User>("user", { Gson().fromJson(it, User::class.java) })
                    .collect { ePrint { "user: $it" } }
            }

            onError = { ePrint { "error: $it" } }
        }
    }
}