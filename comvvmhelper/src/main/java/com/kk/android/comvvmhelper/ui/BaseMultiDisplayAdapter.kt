@file:Suppress("MemberVisibilityCanBePrivate")

package com.kk.android.comvvmhelper.ui

import android.util.SparseIntArray
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kk.android.comvvmhelper.entity.MultiLayoutDisplayConfig

/**
 * @author kuky.
 * @description only worked for GridLayoutManager and fixed item count
 */
abstract class BaseMultiDisplayAdapter<T : Any>(
    dataList: MutableList<T>? = null,
    openDebounce: Boolean = true, debounceDuration: Long = 300
) : BaseRecyclerViewAdapter<T>(dataList, openDebounce, debounceDuration) {
    private var mComplexSpanCount = -1
    private val mTypePool = hashMapOf<Class<*>, MultiLayoutDisplayConfig>()
    private val mLayouts = SparseIntArray()

    fun registerDisplayConfigs(vararg entity: MultiLayoutDisplayConfig) {
        var spc = 1
        entity.forEach {
            mTypePool[it.typeOf] = it
            mLayouts.put(it.viewType, it.layoutId)
            spc *= it.displayCount
        }
        mComplexSpanCount = spc
    }

    fun getComplexSpanCount(): Int {
        check(mComplexSpanCount >= 1) { "Do you have registered your display entities?" }
        return mComplexSpanCount
    }

    override fun layoutId(viewType: Int) = mLayouts[viewType]

    override fun getAdapterItemViewType(position: Int): Int {
        return mTypePool[mDataList[position].javaClass]?.viewType ?: -1
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.layoutManager as? GridLayoutManager)?.let { layoutManager ->
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = findSpanCount(position)
            }
        }
    }

    private fun findSpanCount(position: Int): Int {
        mTypePool.forEach { entry ->
            if (mDataList[position].javaClass == entry.key) {
                return getComplexSpanCount() / entry.value.displayCount
            }
        }
        return getComplexSpanCount()
    }
}
