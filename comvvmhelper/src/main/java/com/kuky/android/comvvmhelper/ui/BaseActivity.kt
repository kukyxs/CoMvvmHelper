@file:Suppress("MemberVisibilityCanBePrivate")

package com.kuky.android.comvvmhelper.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kuky.android.comvvmhelper.helper.ActivityStackManager
import com.kuky.android.comvvmhelper.listener.PermissionDenied
import com.kuky.android.comvvmhelper.listener.PermissionGranted
import com.kuky.android.comvvmhelper.utils.setColorForStatusBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.reflect.ParameterizedType

/**
 * @author kuky.
 * @description
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(), CoroutineScope by MainScope() {
    private var permissionDenied: PermissionDenied? = null
    private var permissionGranted: PermissionGranted? = null

    protected var mBinding: VB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStackManager.addActivity(this)
        if (enabledTransparentStatusBar()) setColorForStatusBar(this, ContextCompat.getColor(this, android.R.color.transparent))
        generateContentView()
        initActivity(savedInstanceState)
    }

    private fun generateContentView() {
        val clazz = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
        if (clazz != ViewDataBinding::class.java && ViewDataBinding::class.java.isAssignableFrom(clazz)) {
            mBinding = DataBindingUtil.setContentView(this, layoutId())
            mBinding?.lifecycleOwner = this
        } else {
            setContentView(layoutId())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        mBinding?.unbind()
        ActivityStackManager.removeActivity(this)
    }

    abstract fun layoutId(): Int

    abstract fun initActivity(savedInstanceState: Bundle?)

    open fun enabledTransparentStatusBar() = false

    fun startActivity(clazz: Class<out Activity>) = startActivity(Intent(this, clazz))

    fun startActivityForResult(clazz: Class<out Activity>, code: Int) = startActivityForResult(Intent(this, clazz), code)

    fun <T : ViewModel> getViewModel(clazz: Class<T>): T =
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(clazz)

    fun permissionsRequest(permissions: Array<String>, granted: PermissionGranted? = null, denied: PermissionDenied? = null) {
        val deniedPermissions = mutableListOf<String>()
        this.permissionGranted = granted
        this.permissionDenied = denied

        permissions
            .filterNot { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
            .forEach { deniedPermissions.add(it) }

        if (deniedPermissions.isEmpty()) permissionGranted?.invoke()
        else ActivityCompat.requestPermissions(this, deniedPermissions.toTypedArray(), 1024)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1024) {
            val deniedPermissions = mutableListOf<String>()
            val grantedPermissions = mutableListOf<String>()

            if (grantResults.isNotEmpty()) {
                for (i in grantResults.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) grantedPermissions.add(permissions[i])
                    else if (grantResults[i] == PackageManager.PERMISSION_DENIED) deniedPermissions.add(permissions[i])
                }
            }

            if (deniedPermissions.isEmpty()) permissionGranted?.invoke()
            else permissionDenied?.invoke(grantedPermissions, deniedPermissions)
        }
    }
}