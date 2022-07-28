package com.kuky.comvvmhelper.ui.dialog

import android.os.Bundle
import android.view.View
import com.kk.android.comvvmhelper.ui.BaseFragment
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.FragmentTestBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TestFragment @Inject constructor() : BaseFragment<FragmentTestBinding>() {

    override fun layoutId() = R.layout.fragment_test

    override fun initFragment(view: View, savedInstanceState: Bundle?) {

    }
}