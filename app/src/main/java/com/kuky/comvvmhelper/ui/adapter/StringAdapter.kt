package com.kuky.comvvmhelper.ui.adapter

import com.kk.android.comvvmhelper.ui.BaseRecyclerViewAdapter
import com.kk.android.comvvmhelper.ui.BaseRecyclerViewHolder
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.RecyclerStringItemBinding

/**
 * @author kuky.
 * @description
 */
class StringAdapter : BaseRecyclerViewAdapter<String>() {

    override fun layoutId(viewType: Int) = R.layout.recycler_string_item

    override fun setVariable(data: String, holder: BaseRecyclerViewHolder, dataPosition: Int, layoutPosition: Int) {
        holder.viewDataBinding<RecyclerStringItemBinding>()?.run {
            text = data
        }
    }
}