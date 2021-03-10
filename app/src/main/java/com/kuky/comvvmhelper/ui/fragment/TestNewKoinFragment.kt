package com.kuky.comvvmhelper.ui.fragment

import android.os.Bundle
import android.view.View
import com.kk.android.comvvmhelper.ui.BaseFragment
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.FragmentTestNewKoinBinding
import com.kuky.comvvmhelper.entity.EntityForKoinScopeTest
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.fragmentScope
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.Scope

/**
 * @author kuky.
 * @description
 */
class TestNewKoinFragment : BaseFragment<FragmentTestNewKoinBinding>(), KoinScopeComponent {

    override val scope: Scope by lazy { fragmentScope() }

    private val aInstance by inject<EntityForKoinScopeTest>()

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