@file:Suppress("MemberVisibilityCanBePrivate")

package com.kk.android.comvvmhelper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.kk.android.comvvmhelper.helper.KLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author kuky.
 * @description
 */
abstract class BaseKeepFragment<VB : ViewDataBinding> : Fragment(), CoroutineScope by MainScope(), KLogger {
    private var mVB: VB? = null
    protected lateinit var mBinding: VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true

        if (mVB == null) {
            mVB = DataBindingUtil.inflate(inflater, layoutId(), container, false)
            actionsOnViewInflate()
        }

        return if (mVB != null) {
            mVB!!.root.apply { (parent as? ViewGroup)?.removeView(this) }
        } else super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkNonNullBinding {
            mBinding = it
            mBinding.lifecycleOwner = this
            initFragment(view, savedInstanceState)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        mBinding.unbind()
        mVB?.unbind()
    }

    protected fun checkNonNullBinding(block: (VB) -> Unit) {
        mVB?.let { block(it) }
    }

    open fun actionsOnViewInflate() {}

    abstract fun layoutId(): Int

    abstract fun initFragment(view: View, savedInstanceState: Bundle?)
}