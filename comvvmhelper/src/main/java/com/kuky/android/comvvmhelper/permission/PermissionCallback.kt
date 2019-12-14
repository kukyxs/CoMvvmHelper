package com.kuky.android.comvvmhelper.permission

/**
 * @author kuky.
 * @description
 */
interface IPermissionCallback {
    fun onGranted() // 全部同意

    fun onDenied(permissions: MutableList<String>) // 权限拒绝

    fun onNeverAsk(permissions: MutableList<String>) // 权限不再询问

    fun onShowRationale(permissionRequest: PermissionRequest) // 首次拒绝后，下次再申请权限时，可增加必要对话框
}

class PermissionCallback(
    private val onGranted: (() -> Unit)? = null,
    private val onDenied: ((MutableList<String>) -> Unit)? = null,
    private val onNeverAsk: ((MutableList<String>) -> Unit)? = null,
    private val onShowRationale: ((PermissionRequest) -> Unit)? = null
) : IPermissionCallback {

    override fun onGranted() {
        onGranted?.invoke()
    }

    override fun onDenied(permissions: MutableList<String>) {
        onDenied?.invoke(permissions)
    }

    override fun onNeverAsk(permissions: MutableList<String>) {
        onNeverAsk?.invoke(permissions)
    }

    override fun onShowRationale(permissionRequest: PermissionRequest) {
        onShowRationale?.invoke(permissionRequest)
    }
}