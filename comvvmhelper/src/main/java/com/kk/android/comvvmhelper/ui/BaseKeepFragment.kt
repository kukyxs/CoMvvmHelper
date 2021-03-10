@file:Suppress("MemberVisibilityCanBePrivate")

package com.kk.android.comvvmhelper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.kk.android.comvvmhelper.anno.FragmentConfig
import com.kk.android.comvvmhelper.extension.layoutToDataBinding
import com.kk.android.comvvmhelper.helper.KLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.koin.androidx.scope.activityScope
import org.koin.androidx.scope.fragmentScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

/**
 * @author kuky.
 * @description fragment to hold state at navigation, resolve recreate
 */
abstract class BaseKeepFragment<VB : ViewDataBinding> : Fragment(), CoroutineScope by MainScope(), KLogger {
    private var mVB: VB? = null
    private val mFragmentConfig by lazy<FragmentConfig?> {
        javaClass.getAnnotation(FragmentConfig::class.java)
    }

    protected val mBinding: VB get() = mVB!!

    var scope: Scope? = null

    val enableScope = mFragmentConfig?.enableKoinScope ?: false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true

        if (mVB == null) {
            mVB = layoutId().layoutToDataBinding(inflater, container)
            mVB?.let {
                actionsOnViewInflate(it)
                it.lifecycleOwner = this
                mVB?.lifecycleOwner = this
            }
        }

        if (enableScope) {
            scope = fragmentScope()
            (activity as? BaseActivity<*>)?.scope?.let { scope?.linkTo(it) }
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
        if (enableScope) scope?.close()
        cancel()
    }

    open fun actionsOnViewInflate(viewBinding: VB) {}

    abstract fun layoutId(): Int

    abstract fun initFragment(view: View, savedInstanceState: Bundle?)

    /**
     * inject lazily
     * @param qualifier - bean qualifier / optional
     * @param mode
     * @param parameters - injection parameters
     */
    inline fun <reified T : Any> inject(
        qualifier: Qualifier? = null,
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
        noinline parameters: ParametersDefinition? = null
    ) = lazy(mode) { get<T>(qualifier, parameters) }

    /**
     * get given dependency
     * @param name - bean name
     * @param scope
     * @param parameters - injection parameters
     */
    inline fun <reified T : Any> get(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null
    ): T = if (!enableScope)
        throw IllegalArgumentException("scope not open")
    else scope!!.get(qualifier, parameters)
}