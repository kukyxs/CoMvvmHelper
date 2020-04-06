package com.kuky.comvvmhelper

import android.Manifest
import android.os.Bundle
import com.kuky.android.comvvmhelper.helper.requestPermissions
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

        requestPermissions {
            putPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            onAllPermissionsGranted = { toast("granted") }

            onPermissionsDenied = { it.forEach { p -> logError("$p denied") } }

            onPermissionsNeverAsked = { it.forEach { p -> logError("$p never ask") } }

            onShowRationale = { request ->
                logError(request)
                alert("请同意必要权限") {
                    positiveButton("ok") { request.retryRequestPermissions() }
                    negativeButton("no") { }
                }.show()
            }
        }
    }
}