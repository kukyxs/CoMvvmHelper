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
    // before use check is it nonnull by [checkNonNullBinding]
    protected var mVB: VB? = null

    // only can be used after `onViewCreated` else will throw not init throwable
    protected lateinit var mBinding: VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true

        if (mVB == null) {
            mVB = DataBindingUtil.inflate(inflater, layoutId(), container, false)
            mVB?.let {
                mBinding = it
                actionsOnViewInflate(it)
                mBinding.lifecycleOwner = this
            }
        }

        return if (mVB != null) {
            mVB!!.root.apply { (parent as? ViewGroup)?.removeView(this) }
        } else super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkNonNullBinding { initFragment(view, savedInstanceState) }
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

    open fun actionsOnViewInflate(viewBinding: VB) {}

    abstract fun layoutId(): Int

    abstract fun initFragment(view: View, savedInstanceState: Bundle?)
}