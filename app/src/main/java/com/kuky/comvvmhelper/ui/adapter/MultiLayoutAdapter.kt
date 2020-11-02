package com.kuky.comvvmhelper.ui.adapter

import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes
import com.kk.android.comvvmhelper.ui.BaseRecyclerViewAdapter
import com.kk.android.comvvmhelper.ui.BaseViewHolder
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.RecyclerIntItemBinding
import com.kuky.comvvmhelper.databinding.RecyclerStringItemBinding
import com.kuky.comvvmhelper.entity.MultiLayoutEntity

/**
 * @author kuky.
 * @description
 */
class MultiLayoutAdapter : BaseRecyclerViewAdapter<MultiLayoutEntity>() {

    override fun layoutId(viewType: Int) = (viewType == MultiLayoutEntity.IntLayout.type)
        .yes { R.layout.recycler_int_item }
        .otherwise { R.layout.recycler_string_item }

    override fun getAdapterItemViewType(position: Int) = (position % 2 == 0)
        .yes { MultiLayoutEntity.IntLayout.type }
        .otherwise { MultiLayoutEntity.StringLayout.type }

    override fun setVariable(data: MultiLayoutEntity, holder: BaseViewHolder, dataPosition: Int, layoutPosition: Int) {
        when (data) {
            is MultiLayoutEntity.IntLayout ->
                holder.viewDataBinding<RecyclerIntItemBinding>()?.text = "IntLayoutItem #$dataPosition#"

            is MultiLayoutEntity.StringLayout ->
                holder.viewDataBinding<RecyclerStringItemBinding>()?.text = "StringLayoutItem #$dataPosition#"
        }
    }
}