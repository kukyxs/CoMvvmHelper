package com.kuky.comvvmhelper.ui.adapter

import com.kk.android.comvvmhelper.ui.BaseRecyclerViewAdapter
import com.kk.android.comvvmhelper.ui.BaseRecyclerViewHolder
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.RecyclerGuideDisplayItemBinding
import com.kuky.comvvmhelper.entity.GuideDisplay

/**
 * @author kuky.
 * @description
 */
class GuideAdapter(items: MutableList<GuideDisplay>) : BaseRecyclerViewAdapter<GuideDisplay>(items) {

    override fun layoutId(viewType: Int) = R.layout.recycler_guide_display_item

    override fun setVariable(data: GuideDisplay, holder: BaseRecyclerViewHolder, dataPosition: Int, layoutPosition: Int) {
        holder.viewDataBinding<RecyclerGuideDisplayItemBinding>()?.run {
            display = data
        }
    }
}