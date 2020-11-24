package com.kk.android.comvvmhelper.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author kuky.
 * @description
 */
open class BaseViewPager2FragmentAdapter : FragmentStateAdapter {
    private var mFragments = mutableListOf<Fragment>()

    constructor(holder: Fragment, fragments: MutableList<Fragment>)
            : super(holder) {
        mFragments = fragments
    }

    constructor(holder: FragmentActivity, fragments: MutableList<Fragment>)
            : super(holder) {
        mFragments = fragments
    }

    constructor(manager: FragmentManager, lifecycle: Lifecycle, fragments: MutableList<Fragment>)
            : super(manager, lifecycle) {
        mFragments = fragments
    }

    override fun getItemCount() = mFragments.size

    override fun createFragment(position: Int) = mFragments[position]
}