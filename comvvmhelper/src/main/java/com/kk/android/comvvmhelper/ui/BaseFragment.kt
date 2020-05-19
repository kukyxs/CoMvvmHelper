@file:Suppress("MemberVisibilityCanBePrivate")

package com.kk.android.comvvmhelper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author kuky.
 * @description
 */
abstract class BaseFragment<VB : ViewDataBinding> : Fragment(), CoroutineScope by MainScope() {

    protected lateinit var mBinding: VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
        cancel()
    }

    abstract fun layoutId(): Int

    abstract fun initFragment(view: View, savedInstanceState: Bundle?)

    fun <T : ViewModel> getViewModel(clazz: Class<T>): T =
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(clazz)

    fun <T : ViewModel> getSharedViewModel(clazz: Class<T>): T =
        ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(clazz)
}