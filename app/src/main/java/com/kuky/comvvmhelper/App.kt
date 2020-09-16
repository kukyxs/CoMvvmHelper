package com.kuky.comvvmhelper

import android.app.Application
import com.kk.android.comvvmhelper.koinInit
import org.koin.dsl.module

/**
 * @author kuky.
 * @description
 */
class App : Application() {
    private val viewModelModule = module { }

    override fun onCreate() {
        super.onCreate()

//        koinInit { koinModules = listOf(viewModelModule) }
        koinInit("cov.properties") { koinModules(viewModelModule) }
    }
}

