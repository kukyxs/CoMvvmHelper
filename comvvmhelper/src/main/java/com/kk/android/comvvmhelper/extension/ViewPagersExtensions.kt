package com.kk.android.comvvmhelper.extension

import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * @author kuky.
 * @description Extensions For ViewPager and ViewPager2
 */
inline fun ViewPager.doOnPageSelected(crossinline action: (Int) -> Unit) =
    addPageChangeListener(onPageSelected = action)

inline fun ViewPager.doOnScroller(crossinline action: (Int, Float, Int) -> Unit) =
    addPageChangeListener(onPageScrolled = action)

inline fun ViewPager.doOnScrollStateChanged(crossinline action: (Int) -> Unit) =
    addPageChangeListener(onPageScrollStateChanged = action)

inline fun ViewPager.addPageChangeListener(
    crossinline onPageScrolled: (Int, Float, Int) -> Unit = { _, _, _ -> },
    crossinline onPageSelected: (Int) -> Unit = {},
    crossinline onPageScrollStateChanged: (Int) -> Unit = {}
): ViewPager.OnPageChangeListener {
    val listener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            onPageScrollStateChanged(state)
        }
    }
    addOnPageChangeListener(listener)
    return listener
}

///////////////////////////////////////
// Extensions for ViewPager2 /////////
/////////////////////////////////////
fun ViewPager2.bindWithTabLayout(
    tabLayout: TabLayout,
    bind: (TabLayout.Tab, Int) -> Unit = { _, _ -> }
) = TabLayoutMediator(tabLayout, this) { tab, position -> bind(tab, position) }

inline fun ViewPager2.doOnPageSelected(crossinline action: (Int) -> Unit) =
    registerPageChangeCallback(onPageSelected = action)

inline fun ViewPager2.doOnScroller(crossinline action: (Int, Float, Int) -> Unit) =
    registerPageChangeCallback(onPageScrolled = action)

inline fun ViewPager2.doOnScrollStateChanged(crossinline action: (Int) -> Unit) =
    registerPageChangeCallback(onPageScrollStateChanged = action)

inline fun ViewPager2.registerPageChangeCallback(
    crossinline onPageScrolled: (Int, Float, Int) -> Unit = { _, _, _ -> },
    crossinline onPageSelected: (Int) -> Unit = {},
    crossinline onPageScrollStateChanged: (Int) -> Unit = {}
): ViewPager2.OnPageChangeCallback {
    val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            onPageScrollStateChanged(state)
        }
    }
    registerOnPageChangeCallback(callback)
    return callback
}

fun ViewPager2.unregisterPageChangeCallback(callback: ViewPager2.OnPageChangeCallback) {
    unregisterOnPageChangeCallback(callback)
}

