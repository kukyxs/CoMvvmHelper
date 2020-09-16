package com.kk.android.comvvmhelper.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kk.android.comvvmhelper.helper.KLogger

/**
 * @author kuky.
 * @description adapter for ViewPager with fragment
 */
abstract class BaseFragmentPagerAdapter(
    fm: FragmentManager, fragments: MutableList<out Fragment>, titles: Array<String>? = null
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT), KLogger {

    private var mFragments = fragments
    private var mTitles = titles

    init {
        if (mTitles.isNullOrEmpty())
            mTitles = Array(fragments.size) { "" }
    }

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getCount(): Int = mFragments.size

    override fun getPageTitle(position: Int): CharSequence? =
        if (mTitles.isNullOrEmpty()) super.getPageTitle(position) else mTitles?.get(position) ?: ""
}