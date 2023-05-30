package com.kuky.comvvmhelper.ui.adapter

import com.kk.android.comvvmhelper.ui.BaseRecyclerViewAdapter
import com.kk.android.comvvmhelper.ui.BaseRecyclerViewHolder
import com.kuky.comvvmhelper.R
import javax.inject.Inject

class FooterAdapter @Inject constructor() : BaseRecyclerViewAdapter<Unit>(mutableListOf(Unit)) {

    override fun layoutId(viewType: Int) = R.layout.recycler_foot_view

    override fun setVariable(data: Unit, holder: BaseRecyclerViewHolder, dataPosition: Int, layoutPosition: Int) {}

    fun appendFooter() {
        val dataList = mutableListOf<Unit>().apply {
            addAll(mDataList)
            add(Unit)
        }

        updateAdapterDataListWithAnim(dataList)
    }

    fun removeFooter(position: Int = 0) {
        if (position > mDataList.size - 1) return
        val total = mDataList.size
        mDataList.toMutableList().removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, total - mDataList.size)
    }
}