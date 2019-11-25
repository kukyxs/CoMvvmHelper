package com.kuky.android.comvvmhelper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.kuky.android.comvvmhelper.listener.PermissionDenied
import com.kuky.android.comvvmhelper.listener.PermissionGranted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.reflect.ParameterizedType

/**
 * @author kuky.
 * @description
 */
abstract class BaseFragment<VB : ViewDataBinding> : Fragment(), CoroutineScope by MainScope() {

    protected var mBinding: VB? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val clazz = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>

        return if (clazz != ViewDataBinding::class.java && ViewDataBinding::class.java.isAssignableFrom(clazz)) {
            mBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
            mBinding?.lifecycleOwner = this
            mBinding?.root
        } else inflater.inflate(layoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding?.unbind()
        cancel()
    }

    abstract fun layoutId(): Int

    abstract fun initFragment(view: View, savedInstanceState: Bundle?)

    fun permissionsRequest(permissions: Array<String>, granted: PermissionGranted? = null, denied: PermissionDenied? = null) =
        if (requireActivity() is BaseActivity<*>)
            (requireActivity() as BaseActivity<*>).permissionsRequest(permissions, granted, denied)
        else throw IllegalArgumentException("Attached activity is not CorBaseActivity and check it")
}