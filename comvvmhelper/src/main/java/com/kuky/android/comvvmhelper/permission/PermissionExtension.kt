package com.kuky.android.comvvmhelper.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * @author kuky.
 * @description
 */

private const val TAG = "base.permission.extension"

fun FragmentActivity.onRuntimePermissionsRequest(vararg permissions: String) {
    ActivityCompat.requestPermissions(this, permissions, 0xFF)
}

fun Activity.permissionGranted(permission: String) =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun FragmentActivity.onRuntimePermissionsRequest(permissions: Array<out String>, callback: PermissionCallback) {
    val requestCode = PermissionMap.put(callback)
    val needRequestPermissions = permissions.filterNot { permissionGranted(it) }

    if (needRequestPermissions.isEmpty()) {
        callback.onGranted()
    } else {
        val shouldShowRationalPermissions = mutableListOf<String>()
        val shouldNotShowRationalPermissions = mutableListOf<String>()

        permissions.forEach {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, it)) {
                shouldShowRationalPermissions.add(it)
            } else {
                shouldNotShowRationalPermissions.add(it)
            }
        }

        if (shouldShowRationalPermissions.isNotEmpty()) {
            callback.onShowRationale(
                PermissionRequest(
                    getPermissionFragment(this),
                    shouldShowRationalPermissions, requestCode
                )
            )
        }

        if (shouldNotShowRationalPermissions.isNotEmpty()) {
            getPermissionFragment(this)
                .requestPermissionsByFragment(
                    shouldNotShowRationalPermissions.toTypedArray(), requestCode
                )
        }
    }
}

private fun getPermissionFragment(activity: FragmentActivity): BasePermissionFragment =
    activity.supportFragmentManager.findFragmentByTag(TAG) as? BasePermissionFragment
        ?: BasePermissionFragment().apply {
            activity.supportFragmentManager.beginTransaction().add(this, TAG).commitNowAllowingStateLoss()
        }