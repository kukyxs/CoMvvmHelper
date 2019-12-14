package com.kuky.comvvmhelper

import android.Manifest
import android.os.Bundle
import com.kuky.android.comvvmhelper.permission.PermissionCallback
import com.kuky.android.comvvmhelper.permission.onRuntimePermissionsRequest
import com.kuky.android.comvvmhelper.ui.BaseActivity
import com.kuky.android.comvvmhelper.utils.logError
import com.kuky.comvvmhelper.databinding.ActivityMainBinding
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

@ObsoleteCoroutinesApi
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun layoutId(): Int = R.layout.activity_main

    override fun initActivity(savedInstanceState: Bundle?) {

        onRuntimePermissionsRequest(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PermissionCallback({
                toast("granted")
            }, { denied ->
                denied.forEach { logError("$it denied") }
            }, { never ->
                never.forEach { logError("$it never ask") }
            }, { request ->
                logError(request)
                alert("请同意必要权限") {
                    positiveButton("ok") { request.retry() }
                    negativeButton("no") { }
                }.show()
            })
        )
    }
}