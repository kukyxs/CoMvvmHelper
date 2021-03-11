package com.kuky.comvvmhelper.ui.fragment

import android.os.Bundle
import android.view.View
import com.kk.android.comvvmhelper.extension.createScopeAndLink
import com.kk.android.comvvmhelper.extension.scopeInject
import com.kk.android.comvvmhelper.ui.BaseFragment
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.FragmentTestNewKoinBinding
import com.kuky.comvvmhelper.entity.EntityForKoinScopeTest
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.Scope

/**
 * @author kuky.
 * @description
 */
class TestNewKoinFragment : BaseFragment<FragmentTestNewKoinBinding>(), KoinScopeComponent {

    override val scope: Scope by lazy { createScopeAndLink() }

    private val aInstance by scopeInject<EntityForKoinScopeTest>()

    override fun layoutId() = R.layout.fragment_test_new_koin

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
        aInstance.print()
        mBinding.resultDisplay.text = aInstance.result()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.close()
    }
}