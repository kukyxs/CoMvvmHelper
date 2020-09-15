package com.kk.android.comvvmhelper.helper

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author kuky.
 * @description 权限申请
 */
private const val K_FRAGMENT_TAG = "base.k.permission.fragment.tag"
private const val K_REQUEST_CODE = 0x00
private const val K_SETTING_REQUEST_CODE = 0xF1
private const val K_OVERLAY_REQUEST_CODE = 0xF2

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
fun FragmentActivity.requestWriteSettings(permissionResult: PermissionGrantedCallback) {
    val callback = PermissionGrantedResult(isPermissionGranted = permissionResult, activity = this)
    PermissionMap.putCall(K_SETTING_REQUEST_CODE, permissionResult)
    getPermissionFragment(callback).requestWriteSettingPermission()
}

@RequiresApi(Build.VERSION_CODES.M)
fun Fragment.requestWriteSettings(permissionResult: PermissionGrantedCallback) {
    val callback = PermissionGrantedResult(
        isPermissionGranted = permissionResult,
        activity = requireActivity(), fragment = this
    )
    PermissionMap.putCall(K_SETTING_REQUEST_CODE, permissionResult)
    getPermissionFragment(callback).requestWriteSettingPermission()
}

// request alert window permission
@RequiresApi(Build.VERSION_CODES.M)
fun FragmentActivity.requestOverlay(permissionResult: PermissionGrantedCallback) {
    val callback = PermissionGrantedResult(isPermissionGranted = permissionResult, activity = this)
    PermissionMap.putCall(K_OVERLAY_REQUEST_CODE, permissionResult)
    getPermissionFragment(callback).requestOverlayPermission()
}

@RequiresApi(Build.VERSION_CODES.M)
fun Fragment.requestOverlay(permissionResult: PermissionGrantedCallback) {
    val callback = PermissionGrantedResult(
        isPermissionGranted = permissionResult,
        activity = requireActivity(), fragment = this
    )
    PermissionMap.putCall(K_OVERLAY_REQUEST_CODE, permissionResult)
    getPermissionFragment(callback).requestOverlayPermission()
}

fun Context.toAppDetailSettings(targetAppPack: String? = null) {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", targetAppPack ?: packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}

@RequiresApi(Build.VERSION_CODES.M)
fun Context.isWriteSettingsEnabled() = Settings.System.canWrite(this)

@RequiresApi(Build.VERSION_CODES.M)
fun Context.isAlertWindowEnabled() = Settings.canDrawOverlays(this)

// 权限是否已授权
private fun FragmentActivity.permissionGranted(permission: String) =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

// 请求权限
@RequiresApi(Build.VERSION_CODES.M)
private fun onRuntimePermissionsRequest(callback: PermissionCallback) {
    val permissions = callback.permissions

    if (permissions.isEmpty()) return

    val requestCode = PermissionMap.put(callback)
    val needRequestPermissions = permissions.filterNot { callback.activity.permissionGranted(it) }

    needRequestPermissions.isEmpty().yes {
        callback.onAllPermissionsGranted()
    }.otherwise {
        val shouldShowRationalPermissions = mutableListOf<String>() // 权限被拒绝后，可弹出信息告诉用户需要权限的理由
        val shouldNotShowRationalPermissions = mutableListOf<String>() // 首次申请的权限

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
 * 权限申请 Fragment
 */
class KPermissionFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun requestPermissionsByFragment(permissions: Array<out String>, requestCode: Int) =
        requestPermissions(permissions, requestCode)

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestWriteSettingPermission() = startActivityForResult(
        Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
            data = Uri.parse("package:" + requireContext().packageName)
        }, K_SETTING_REQUEST_CODE
    )

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestOverlayPermission() = startActivityForResult(
        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
            data = Uri.parse("package:" + requireContext().packageName)
        }, K_OVERLAY_REQUEST_CODE
    )

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val neverAskedPermissions = mutableListOf<String>()
        val deniedPermissions = mutableListOf<String>()
        val grantedPermissions = mutableListOf<String>()

        permissions.forEachIndexed { index, permission ->
            if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                shouldShowRequestPermissionRationale(permission)
                    .yes { deniedPermissions }.otherwise { neverAskedPermissions }.add(permission)
            } else if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permission)
            }
        }

        PermissionMap.fetch(requestCode)?.let {
            if (neverAskedPermissions.isNotEmpty()) it.onPermissionsNeverAsked(neverAskedPermissions)

            if (deniedPermissions.isNotEmpty()) it.onPermissionsDenied(deniedPermissions)

            if (neverAskedPermissions.isEmpty() && deniedPermissions.isEmpty()) it.onAllPermissionsGranted()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val callback = PermissionMap.fetchCall(requestCode) ?: return
        when (requestCode) {
            K_SETTING_REQUEST_CODE -> callback.isPermissionGranted(Settings.System.canWrite(requireContext()))
            K_OVERLAY_REQUEST_CODE -> callback.isPermissionGranted(Settings.canDrawOverlays(requireContext()))
        }
    }
}

/**
 * 根据 PermissionCallback 生成 code
 */
private object PermissionMap {
    private val atomicInteger = AtomicInteger(K_REQUEST_CODE)
    private val map = mutableMapOf<Int, PermissionCallback>()
    private var grantedMap = mutableMapOf<Int, PermissionGrantedCallback>()

    fun put(callback: PermissionCallback) =
        atomicInteger.getAndIncrement().apply {
            map[this] = callback
        }

    fun fetch(requestCode: Int): PermissionCallback? =
        map[requestCode].apply {
            map.remove(requestCode)
        }

    fun putCall(code: Int, callResult: PermissionGrantedCallback) {
        grantedMap[code] = callResult
    }

    fun fetchCall(code: Int): PermissionGrantedCallback? = grantedMap[code]
}

/**
 * 首次申请拒绝，再次申请回调
 */
data class PermissionRequestAgain(
    private val kPermissionFragment: KPermissionFragment,
    private val permissions: MutableList<String>,
    private val requestCode: Int
) {
    fun retryRequestPermissions() = kPermissionFragment.requestPermissions(permissions.toTypedArray(), requestCode)
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
 * 权限申请回调, 请勿手动设置 activity, fragment 参数
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