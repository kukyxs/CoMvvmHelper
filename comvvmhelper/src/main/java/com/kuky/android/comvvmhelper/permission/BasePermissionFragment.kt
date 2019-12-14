package com.kuky.android.comvvmhelper.permission

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * @author kuky.
 * @description
 */
class BasePermissionFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun requestPermissionsByFragment(permissions: Array<out String>, requestCode: Int) {
        requestPermissions(permissions, requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val never = mutableListOf<String>()
        val granted = mutableListOf<String>()
        val denied = mutableListOf<String>()

        permissions.forEachIndexed { index, s ->
            if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(s)) denied.add(s) else never.add(s)
            } else {
                granted.add(s)
            }
        }

        PermissionMap.fetch(requestCode)?.let {
            if (denied.isNotEmpty()) it.onDenied(denied)

            if (never.isNotEmpty()) it.onNeverAsk(never)

            if (denied.isEmpty() && never.isEmpty()) it.onGranted()
        }
    }
}