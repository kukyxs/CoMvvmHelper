package com.kk.android.comvvmhelper.ui

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @author kuky.
 * @description
 */
open class BaseViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    @Suppress("UNCHECKED_CAST")
    fun <T : ViewDataBinding> getViewDataBinding(): T? = binding as? T
}