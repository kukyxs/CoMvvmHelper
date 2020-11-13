package com.kk.android.comvvmhelper.ui

import android.util.SparseIntArray
import com.kk.android.comvvmhelper.listener.MultiLayoutImp


/**
 * @author kuky.
 * @description
 */
abstract class BaseMultiLayoutAdapter(
    dataList: MutableList<MultiLayoutImp>? = null,
    openDebounce: Boolean = true, debounceDuration: Long = 300
) : BaseRecyclerViewAdapter<MultiLayoutImp>(dataList, openDebounce, debounceDuration) {

    private val mLayouts = SparseIntArray()

    fun registerAdapterItems(viewType: Int, layoutId: Int) {
        mLayouts.put(viewType, layoutId)
    }

    override fun layoutId(viewType: Int) = mLayouts[viewType]

    override fun getAdapterItemViewType(position: Int) = mDataList[position].viewType()
}