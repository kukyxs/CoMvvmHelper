package com.kuky.comvvmhelper

import android.Manifest
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.kk.android.comvvmhelper.helper.KLogger
import com.kk.android.comvvmhelper.helper.ePrint
import com.kk.android.comvvmhelper.helper.requestPermissions
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kuky.comvvmhelper.databinding.ActivityMainBinding
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

@ObsoleteCoroutinesApi
class MainActivity : BaseActivity<ActivityMainBinding>(), KLogger {

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