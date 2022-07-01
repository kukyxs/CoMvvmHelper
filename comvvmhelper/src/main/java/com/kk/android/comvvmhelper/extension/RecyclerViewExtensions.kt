package com.kk.android.comvvmhelper.extension

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlin.math.min

/**
 * @author kuky.
 * @description
 */
fun RecyclerView.scrollToTop(sizeOneLine: Int = 2, threshold: Int = 10) {
    when (val manager = layoutManager) {
        is LinearLayoutManager -> {
            manager.let {
                val first = it.findFirstCompletelyVisibleItemPosition()
                if (first == 0) return@let

                manager.scrollToPositionWithOffset(min(first, threshold), 0)
                manager.smoothScrollToPosition(this, RecyclerView.State(), 0)
            }
        }

        is GridLayoutManager -> {
            manager.let {
                val first = it.findFirstCompletelyVisibleItemPosition()
                if (first == 0) return@let

                manager.scrollToPositionWithOffset(min(first, threshold), 0)
                manager.smoothScrollToPosition(this@scrollToTop, RecyclerView.State(), 0)
            }
        }

        is StaggeredGridLayoutManager -> {
            manager.let {
                val first = IntArray(sizeOneLine)
                it.findFirstCompletelyVisibleItemPositions(first)
                if (first[0] == 0) return@let

                manager.scrollToPositionWithOffset(min(first[0], threshold), 0)
                manager.smoothScrollToPosition(this@scrollToTop, RecyclerView.State(), 0)
            }
        }
    }
}
