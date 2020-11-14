package com.kuky.comvvmhelper.ui.adapter

import com.kk.android.comvvmhelper.entity.MultiLayoutDisplayConfig
import com.kk.android.comvvmhelper.ui.BaseMultiDisplayAdapter
import com.kk.android.comvvmhelper.ui.BaseRecyclerViewHolder
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.RecyclerFloatItemBinding
import com.kuky.comvvmhelper.databinding.RecyclerIntItemBinding
import com.kuky.comvvmhelper.databinding.RecyclerLongItemBinding
import com.kuky.comvvmhelper.databinding.RecyclerStringItemBinding
import com.kuky.comvvmhelper.entity.*

/**
 * @author kuky.
 * @description
 */
class MultiDisplayAdapter : BaseMultiDisplayAdapter<IMultiDisplay>() {

    init {
        registerDisplayConfigs(
            MultiLayoutDisplayConfig(DisplayTypeOne::class.java, 1, 0xFF01, R.layout.recycler_int_item),
            MultiLayoutDisplayConfig(DisplayTypeTwo::class.java, 3, 0xFF02, R.layout.recycler_long_item),
            MultiLayoutDisplayConfig(DisplayTypeThree::class.java, 5, 0xFF03, R.layout.recycler_float_item),
            MultiLayoutDisplayConfig(DisplayTypeFour::class.java, 6, 0xFF04, R.layout.recycler_string_item)
        )
    }

    override fun setVariable(data: IMultiDisplay, holder: BaseRecyclerViewHolder, dataPosition: Int, layoutPosition: Int) {
        when (data) {
            is DisplayTypeOne ->
                holder.viewDataBinding<RecyclerIntItemBinding>()?.text = "Display One $dataPosition"

            is DisplayTypeTwo ->
                holder.viewDataBinding<RecyclerLongItemBinding>()?.text = "Display Two $dataPosition"

            is DisplayTypeThree ->
                holder.viewDataBinding<RecyclerFloatItemBinding>()?.text = "Display Three $dataPosition"

            is DisplayTypeFour ->
                holder.viewDataBinding<RecyclerStringItemBinding>()?.text = "Display Four $dataPosition"
        }
    }
}