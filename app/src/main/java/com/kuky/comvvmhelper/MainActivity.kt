package com.kuky.comvvmhelper

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.helper.ePrint
import com.kk.android.comvvmhelper.helper.requestPermissions
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kuky.comvvmhelper.databinding.ActivityMainBinding
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

@ActivityConfig(statusBarColor = Color.BLACK)
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun layoutId(): Int = R.layout.activity_main

    override fun initActivity(savedInstanceState: Bundle?) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions {
                putPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_CALENDAR,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

                onAllPermissionsGranted = { toast("granted"); ePrint { "all granted" } }

                onPermissionsDenied = { it.forEach { p -> ePrint { "$p denied" } } }

                onPermissionsNeverAsked = { it.forEach { p -> ePrint { "$p never ask" } } }

                onShowRationale = { request ->
                    ePrint { request }
                    alert("请同意必要权限") {
                        positiveButton("ok") { request.retryRequestPermissions() }
                        negativeButton("no") { }
                    }.show()
                }
            }
        }
    }
}