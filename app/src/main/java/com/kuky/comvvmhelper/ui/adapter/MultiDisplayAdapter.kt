package com.kuky.comvvmhelper.ui.adapter

import com.kk.android.comvvmhelper.entity.MultiDisplayEntity
import com.kk.android.comvvmhelper.ui.BaseMultiItemDisplayAdapter
import com.kk.android.comvvmhelper.ui.BaseViewHolder
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
class MultiDisplayAdapter : BaseMultiItemDisplayAdapter<IMultiDisplay>() {

    init {
        registerDisplayEntities(
            MultiDisplayEntity(DisplayTypeOne::class.java, 1, 0xFF01, R.layout.recycler_int_item),
            MultiDisplayEntity(DisplayTypeTwo::class.java, 3, 0xFF02, R.layout.recycler_long_item),
            MultiDisplayEntity(DisplayTypeThree::class.java, 5, 0xFF03, R.layout.recycler_float_item),
            MultiDisplayEntity(DisplayTypeFour::class.java, 6, 0xFF04, R.layout.recycler_string_item)
        )
    }

    override fun setVariable(data: IMultiDisplay, holder: BaseViewHolder, dataPosition: Int, layoutPosition: Int) {
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