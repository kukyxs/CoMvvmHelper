@file:Suppress("MemberVisibilityCanBePrivate")

package com.kk.android.comvvmhelper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.kk.android.comvvmhelper.extension.layoutToDataBinding
import com.kk.android.comvvmhelper.helper.KLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author kuky.
 * @description fragment to hold state at navigation, resolve recreate
 */
abstract class BaseKeepFragment<VB : ViewDataBinding> : Fragment(), CoroutineScope by MainScope(), KLogger {
    private var mVB: VB? = null
    protected val mBinding: VB get() = mVB!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true

        if (mVB == null) {
            mVB = layoutId().layoutToDataBinding(inflater, container)
            mVB?.let {
                actionsOnViewInflate(it)
                it.lifecycleOwner = this
                mBinding.lifecycleOwner = this
            }
        }

        return mVB?.root?.apply { (parent as? ViewGroup)?.removeView(this) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mVB?.unbind()
        cancel()
    }

    open fun actionsOnViewInflate(viewBinding: VB) {}

    abstract fun layoutId(): Int

    abstract fun initFragment(view: View, savedInstanceState: Bundle?)
}