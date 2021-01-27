package com.kuky.comvvmhelper.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes
import com.kk.android.comvvmhelper.helper.*
import com.kk.android.comvvmhelper.listener.OnErrorReloadListener
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kk.android.comvvmhelper.widget.RequestStatusCode
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.ActivityPermissionDemoBinding
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

@ActivityConfig(statusBarColorString = "#008577")
class PermissionDemoActivity : BaseActivity<ActivityPermissionDemoBinding>() {

    override fun layoutId(): Int = R.layout.activity_permission_demo

    override fun initActivity(savedInstanceState: Bundle?) {

        mBinding.requestStatus = RequestStatusCode.Loading
        mBinding.reload = OnErrorReloadListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                toAppDetailSettings(requestCode = 0xFF01)
            } else {
                mBinding.requestStatus = RequestStatusCode.Succeed
            }
        }

        // request permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions()
        } else {
            mBinding.requestStatus = RequestStatusCode.Succeed
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0xFF01 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBinding.requestStatus = (Manifest.permission.CAMERA.isPermissionNeverShow() ||
                    Manifest.permission.READ_EXTERNAL_STORAGE.isPermissionNeverShow())
                .yes { RequestStatusCode.Error }.otherwise { RequestStatusCode.Succeed }
        }
    }

    private fun String.isPermissionNeverShow() = !permissionGranted() &&
            !ActivityCompat.shouldShowRequestPermissionRationale(this@PermissionDemoActivity, this)

    private fun String.permissionGranted() =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(this@PermissionDemoActivity, this) == PackageManager.PERMISSION_GRANTED

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermissions() {
        requestPermissions {
            putPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

            onAllPermissionsGranted = {
                mBinding.requestStatus = RequestStatusCode.Succeed
                toast("permissions are all granted")
            }

            onPermissionsDenied = {
                mBinding.requestStatus = RequestStatusCode.Empty
                it.forEach { p -> ePrint { "permission [$p] was denied" } }
            }

            onPermissionsNeverAsked = {
                mBinding.requestStatus = RequestStatusCode.Error
                it.forEach { p -> ePrint { "never request permission [$p] again" } }
            }

            onShowRationale = { request ->
                alert("please grant the necessary permissions, or the app will not work") {
                    positiveButton("ok") { request.retryRequestPermissions() }
                    negativeButton("no") { ActivityStackManager.exitApplication() }
                }.show()
            }
        }
    }
}