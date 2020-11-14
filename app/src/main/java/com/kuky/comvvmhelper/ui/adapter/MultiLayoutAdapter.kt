package com.kuky.comvvmhelper.ui.adapter

import com.kk.android.comvvmhelper.listener.MultiLayoutImp
import com.kk.android.comvvmhelper.ui.BaseMultiLayoutAdapter
import com.kk.android.comvvmhelper.ui.BaseRecyclerViewHolder
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.RecyclerIntItemBinding
import com.kuky.comvvmhelper.databinding.RecyclerStringItemBinding
import com.kuky.comvvmhelper.entity.IntLayoutEntity
import com.kuky.comvvmhelper.entity.StringLayoutEntity

/**
 * @author kuky.
 * @description
 */
class MultiLayoutAdapter : BaseMultiLayoutAdapter() {

    init {
        registerAdapterItems(0xFF01, R.layout.recycler_int_item)
        registerAdapterItems(0xFF02, R.layout.recycler_string_item)
    }

    override fun setVariable(data: MultiLayoutImp, holder: BaseRecyclerViewHolder, dataPosition: Int, layoutPosition: Int) {
        when (data) {
            is IntLayoutEntity ->
                holder.viewDataBinding<RecyclerIntItemBinding>()?.text = "IntLayoutItem #$dataPosition#"

            is StringLayoutEntity ->
                holder.viewDataBinding<RecyclerStringItemBinding>()?.text = "StringLayoutItem #$dataPosition#"
        }
    }
}