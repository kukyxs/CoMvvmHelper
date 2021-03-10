package com.kuky.comvvmhelper.ui.fragment

import android.os.Bundle
import android.view.View
import com.kk.android.comvvmhelper.anno.FragmentConfig
import com.kk.android.comvvmhelper.ui.BaseFragment
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.FragmentTestNewKoinBinding
import com.kuky.comvvmhelper.entity.EntityForKoinScopeTest

/**
 * @author kuky.
 * @description
 */
@FragmentConfig(enableKoinScope = true)
class TestNewKoinFragment : BaseFragment<FragmentTestNewKoinBinding>() {

    private val aInstance by inject<EntityForKoinScopeTest>()

    override fun layoutId() = R.layout.fragment_test_new_koin

    override fun initFragment(view: View, savedInstanceState: Bundle?) {
        aInstance.print()
        mBinding.resultDisplay.text = aInstance.result()
    }
}