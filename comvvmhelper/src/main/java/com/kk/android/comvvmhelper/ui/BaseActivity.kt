@file:Suppress("MemberVisibilityCanBePrivate")

package com.kk.android.comvvmhelper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.kk.android.comvvmhelper.helper.ActivityStackManager
import com.kk.android.comvvmhelper.helper.KLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author kuky.
 * @description
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(), CoroutineScope by MainScope(), KLogger {
    protected val mBinding: VB by lazy {
        DataBindingUtil.setContentView(this, layoutId()) as VB
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStackManager.addActivity(this)
        mBinding.lifecycleOwner = this
        initActivity(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        mBinding.unbind()
        ActivityStackManager.removeActivity(this)
    }

    abstract fun layoutId(): Int

    abstract fun initActivity(savedInstanceState: Bundle?)
}