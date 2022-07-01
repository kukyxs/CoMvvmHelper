package com.kk.android.comvvmhelper.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author kuky.
 * @description DSL for permission request
 */
private const val K_FRAGMENT_TAG = "base.k.permission.fragment.tag"
private const val K_REQUEST_CODE = 0x00
private const val K_SETTING_REQUEST_CODE = 0xF1
private const val K_OVERLAY_REQUEST_CODE = 0xF2
private const val K_EXTERNAL_REQUEST_CODE = 0xF3

// request permissions
@RequiresApi(Build.VERSION_CODES.M)
fun FragmentActivity.requestPermissions(init: PermissionCallback.() -> Unit) {
    val callback = PermissionCallback(activity = this).apply(init)
    onRuntimePermissionsRequest(callback)
}

@RequiresApi(Build.VERSION_CODES.M)
fun Fragment.requestPermissions(init: PermissionCallback.() -> Unit) {
    val callback = PermissionCallback(activity = requireActivity(), fragment = this).apply(init)
    onRuntimePermissionsRequest(callback)
}

// request write settings permission
@RequiresApi(Build.VERSION_CODES.M)
fun FragmentActivity.requestWriteSettings(targetAppPackageName: String? = null, permissionResult: PermissionGrantedCallback) {
    val callback = PermissionGrantedResult(isPermissionGranted = permissionResult, activity = this)
    PermissionCodePool.putCall(K_SETTING_REQUEST_CODE, permissionResult)
    getPermissionFragment(callback).requestWriteSettingPermission(targetAppPackageName)
}

@RequiresApi(Build.VERSION_CODES.M)
fun Fragment.requestWriteSettings(targetAppPackageName: String? = null, permissionResult: PermissionGrantedCallback) {
    val callback = PermissionGrantedResult(
        isPermissionGranted = permissionResult,
        activity = requireActivity(), fragment = this
    )
    PermissionCodePool.putCall(K_SETTING_REQUEST_CODE, permissionResult)
    getPermissionFragment(callback).requestWriteSettingPermission(targetAppPackageName)
}

// request alert window permission
@RequiresApi(Build.VERSION_CODES.M)
fun FragmentActivity.requestOverlay(targetAppPackageName: String? = null, permissionResult: PermissionGrantedCallback) {
    val callback = PermissionGrantedResult(isPermissionGranted = permissionResult, activity = this)
    PermissionCodePool.putCall(K_OVERLAY_REQUEST_CODE, permissionResult)
    getPermissionFragment(callback).requestOverlayPermission(targetAppPackageName)
}

@RequiresApi(Build.VERSION_CODES.M)
fun Fragment.requestOverlay(targetAppPackageName: String? = null, permissionResult: PermissionGrantedCallback) {
    val callback = PermissionGrantedResult(
        isPermissionGranted = permissionResult,
        activity = requireActivity(), fragment = this
    )
    PermissionCodePool.putCall(K_OVERLAY_REQUEST_CODE, permissionResult)
    getPermissionFragment(callback).requestOverlayPermission(targetAppPackageName)
}

@RequiresApi(Build.VERSION_CODES.R)
fun FragmentActivity.requestManageExternalPermission(targetAppPackageName: String? = null, permissionResult: PermissionGrantedCallback) {
    val callback = PermissionGrantedResult(isPermissionGranted = permissionResult, activity = this)
    PermissionCodePool.putCall(K_EXTERNAL_REQUEST_CODE, permissionResult)
    getPermissionFragment(callback).requestManageExternalPermission(targetAppPackageName)
}

@RequiresApi(Build.VERSION_CODES.R)
fun Fragment.requestManageExternalPermission(targetAppPackageName: String? = null, permissionResult: PermissionGrantedCallback) {
    val callback = PermissionGrantedResult(
        isPermissionGranted = permissionResult,
        activity = requireActivity(), fragment = this
    )
    PermissionCodePool.putCall(K_EXTERNAL_REQUEST_CODE, permissionResult)
    getPermissionFragment(callback).requestManageExternalPermission(targetAppPackageName)
}

// to app detail settings page
fun Context.toAppDetailSettings(targetAppPackageName: String? = null) {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", targetAppPackageName ?: packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}

fun Activity.toAppDetailSettings(targetAppPackageName: String? = null, requestCode: Int) {
    startActivityForResult(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", targetAppPackageName ?: packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }, requestCode)
}

// is write settings is granted
fun Context.isWriteSettingsEnabled() =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.System.canWrite(this)

// is overlay is granted
fun Context.isAlertWindowEnabled() =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)

fun isManageExternalEnabled() =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager()

fun isManageExternalEnabled(path: File) =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager(path)

// is permission has granted
private fun FragmentActivity.permissionGranted(permission: String) =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

@RequiresApi(Build.VERSION_CODES.M)
private fun onRuntimePermissionsRequest(callback: PermissionCallback) {
    val permissions = callback.permissions

    if (permissions.isEmpty()) {
        callback.onAllPermissionsGranted()
        return
    }

    val requestCode = PermissionCodePool.put(callback)
    val needRequestPermissions = permissions.filterNot { callback.activity.permissionGranted(it) }

    if (needRequestPermissions.isEmpty()) {
        callback.onAllPermissionsGranted()
    } else {
        val shouldShowRationalPermissions = mutableListOf<String>()
        val shouldNotShowRationalPermissions = mutableListOf<String>()

        permissions.forEach {
            val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(callback.activity, it)

            if (showRationale) {
                shouldShowRationalPermissions.add(it)
            } else {
                shouldNotShowRationalPermissions.add(it)
            }
        }

        if (shouldShowRationalPermissions.isNotEmpty()) {
            callback.onShowRationale(
                PermissionRequestAgain(
                    getPermissionFragment(callback),
                    shouldShowRationalPermissions,
                    requestCode
                )
            )
        }

        if (shouldNotShowRationalPermissions.isNotEmpty()) {
            getPermissionFragment(callback)
                .requestPermissionsByFragment(
                    shouldNotShowRationalPermissions.toTypedArray(),
                    requestCode
                )
        }
    }
}

// find permission fragment
private fun getPermissionFragment(callback: PermissionCallback): KPermissionFragment {
    val fragmentManager = callback.fragment?.childFragmentManager ?: callback.activity.supportFragmentManager
    return fragmentManager.findFragmentByTag(K_FRAGMENT_TAG) as? KPermissionFragment
        ?: KPermissionFragment().apply {
            fragmentManager.beginTransaction()
                .add(this, K_FRAGMENT_TAG).commitNowAllowingStateLoss()
        }
}

private fun getPermissionFragment(callResult: PermissionGrantedResult): KPermissionFragment {
    val fragmentManager = callResult.fragment?.childFragmentManager ?: callResult.activity.supportFragmentManager
    return fragmentManager.findFragmentByTag(K_FRAGMENT_TAG) as? KPermissionFragment
        ?: KPermissionFragment().apply {
            fragmentManager.beginTransaction()
                .add(this, K_FRAGMENT_TAG).commitNowAllowingStateLoss()
        }
}

/**
 * Fragment for request permission
 */
class KPermissionFragment : Fragment() {
    private var mCode = 0
    private var mRequestCode = -1

    private val requestPermissionLaunch = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionResultEntry ->
        val neverAskedPermissions = mutableListOf<String>()
        val deniedPermissions = mutableListOf<String>()
        val grantedPermissions = mutableListOf<String>()

        permissionResultEntry.entries.forEach {
            val permission = it.key
            val result = it.value
            if (!result) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    deniedPermissions.add(permission)
                } else {
                    neverAskedPermissions.add(permission)
                }
            } else {
                grantedPermissions.add(permission)
            }
        }

        PermissionCodePool.fetch(mCode)?.let {
            if (neverAskedPermissions.isNotEmpty()) it.onPermissionsNeverAsked(neverAskedPermissions)

            if (deniedPermissions.isNotEmpty()) it.onPermissionsDenied(deniedPermissions)

            if (neverAskedPermissions.isEmpty() && deniedPermissions.isEmpty()) it.onAllPermissionsGranted()
        }
    }

    private val requestForResultLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@registerForActivityResult
        val callback = PermissionCodePool.fetchCall(mRequestCode) ?: return@registerForActivityResult
        when (mRequestCode) {
            K_SETTING_REQUEST_CODE -> callback.isPermissionGranted(Settings.System.canWrite(requireContext()))
            K_OVERLAY_REQUEST_CODE -> callback.isPermissionGranted(Settings.canDrawOverlays(requireContext()))
            K_EXTERNAL_REQUEST_CODE -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                callback.isPermissionGranted(Environment.isExternalStorageManager())
            }
        }
    }

    fun requestPermissionsByFragment(permissions: Array<String>, requestCode: Int) =
        requestPermissionLaunch.launch(permissions).apply { mCode = requestCode }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestWriteSettingPermission(packageName: String? = null) =
        requestForResultLaunch.launch(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
            data = Uri.parse("package:${packageName ?: requireContext().packageName}")
        }).run {
            mRequestCode = K_SETTING_REQUEST_CODE
        }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestOverlayPermission(packageName: String? = null) =
        requestForResultLaunch.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
            data = Uri.parse("package:${packageName ?: requireContext().packageName}")
        }).run {
            mRequestCode = K_OVERLAY_REQUEST_CODE
        }

    @RequiresApi(Build.VERSION_CODES.R)
    fun requestManageExternalPermission(packageName: String? = null) {
        requestForResultLaunch.launch(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
            data = Uri.parse("package:${packageName ?: requireContext().packageName}")
        }).run {
            mRequestCode = K_EXTERNAL_REQUEST_CODE
        }
    }
}

/**
 * request code pool
 */
private object PermissionCodePool {
    private val atomicCode = AtomicInteger(K_REQUEST_CODE)
    private val codePool = mutableMapOf<Int, PermissionCallback>()
    private var grantedPool = mutableMapOf<Int, PermissionGrantedCallback>()

    fun put(callback: PermissionCallback) =
        atomicCode.getAndIncrement().apply {
            codePool[this] = callback
        }

    fun fetch(requestCode: Int): PermissionCallback? =
        codePool[requestCode].apply {
            codePool.remove(requestCode)
        }

    fun putCall(code: Int, callResult: PermissionGrantedCallback) {
        grantedPool[code] = callResult
    }

    fun fetchCall(code: Int): PermissionGrantedCallback? = grantedPool[code]
}

data class PermissionRequestAgain(
    private val kPermissionFragment: KPermissionFragment,
    private val permissions: MutableList<String>,
    private val requestCode: Int
) {
    fun retryRequestPermissions() = /*kPermissionFragment.requestPermissions(permissions.toTypedArray(), requestCode)*/
        kPermissionFragment.requestPermissionsByFragment(permissions.toTypedArray(), requestCode)

}

/**
 * Write Settings / Overlay request callback
 */
fun interface PermissionGrantedCallback {
    fun isPermissionGranted(granted: Boolean)
}

data class PermissionGrantedResult(
    var isPermissionGranted: PermissionGrantedCallback = PermissionGrantedCallback { },
    internal var activity: FragmentActivity, internal var fragment: Fragment? = null
)


/**
 * do not set activity or fragment by manual
 */
data class PermissionCallback(
    var permissions: MutableList<String> = mutableListOf(),
    var onAllPermissionsGranted: () -> Unit = {},
    var onPermissionsDenied: (MutableList<String>) -> Unit = {},
    var onPermissionsNeverAsked: (MutableList<String>) -> Unit = {},
    var onShowRationale: (PermissionRequestAgain) -> Unit = { it.retryRequestPermissions() },
    internal var activity: FragmentActivity, internal var fragment: Fragment? = null
) {
    fun putPermissions(vararg ps: String) {
        permissions = ps.toMutableList()
    }
}