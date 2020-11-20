@file:Suppress("CHANGING_ARGUMENTS_EXECUTION_ORDER_FOR_NAMED_VARARGS")

package com.kk.android.comvvmhelper.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kk.android.comvvmhelper.listener.OnRecyclerItemClickListener
import com.kk.android.comvvmhelper.listener.OnRecyclerItemLongClickListener
import com.kk.android.comvvmhelper.ui.BaseRecyclerViewAdapter

/**
 * @author kuky.
 * @description BindingAdapter for RecyclerView
 */

/**
 * @param adapter  open debounced is setting by adapter construction [BaseRecyclerViewAdapter.openDebounce], [BaseRecyclerViewAdapter.debounceDuration]
 * @param listener item click or item debounced click
 * @param longListener item long click
 */
@BindingAdapter(value = ["bind:recyclerAdapter", "bind:onRecyclerItemClick", "bind:onRecyclerItemLongClick"], requireAll = false)
fun bindRecyclerAdapter(
    recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?,
    listener: OnRecyclerItemClickListener?,
    longListener: OnRecyclerItemLongClickListener?
) {
    adapter?.let { recyclerView.adapter = it }

    listener?.let { (recyclerView.adapter as? BaseRecyclerViewAdapter<*>)?.onItemClickListener = it }

    longListener?.let { (recyclerView.adapter as? BaseRecyclerViewAdapter<*>)?.onItemLongClickListener = it }
}

/**
 * @param decor an item decoration for RecyclerView
 */
@BindingAdapter("bind:recyclerDivider")
fun bindRecyclerDivider(recyclerView: RecyclerView, decor: RecyclerView.ItemDecoration) {
    recyclerView.addItemDecoration(decor)
}