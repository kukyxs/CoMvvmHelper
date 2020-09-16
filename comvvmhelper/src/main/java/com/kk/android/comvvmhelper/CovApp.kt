package com.kk.android.comvvmhelper

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

/**
 * @author kuky.
 * @description
 */
fun Application.koinInit(vararg modules: Module) {
    koinInit { koinModules = modules.asList() }
}

fun Application.koinInit(
    koinPropertiesFile: String = "koin.properties",
    creator: ModuleCreator.() -> Unit
) {
    startKoin {
        androidLogger(Level.ERROR) // if level is not ERROR, it will be crashed
        androidContext(this@koinInit)
        androidFileProperties(koinPropertiesFile)
        modules(ModuleCreator().apply(creator).koinModules)
    }
}

data class ModuleCreator(
    var koinModules: List<Module> = listOf()
) {
    fun koinModules(vararg modules: Module) {
        this.koinModules = modules.asList()
    }
}
