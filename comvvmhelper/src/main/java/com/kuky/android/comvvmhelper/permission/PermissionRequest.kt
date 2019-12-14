package com.kuky.android.comvvmhelper.permission

/**
 * @author kuky.
 * @description
 */
data class PermissionRequest(
    private val basePermissionFragment: BasePermissionFragment,
    private val permissions: MutableList<String>,
    private val requestCode: Int
) {
    fun retry() = basePermissionFragment
        .requestPermissionsByFragment(permissions.toTypedArray(), requestCode)
}