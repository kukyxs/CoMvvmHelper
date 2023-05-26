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
 * @param adapter  open debounced is setting by adapter construction
 * @param listener item click or item debounced click
 * @param longListener item long click
 */
@BindingAdapter(value = ["recyclerAdapter", "onRecyclerItemClick", "onRecyclerItemLongClick", "recyclerDivider"], requireAll = false)
fun bindRecyclerAdapter(
    recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?,
    listener: OnRecyclerItemClickListener?,
    longListener: OnRecyclerItemLongClickListener?,
    decor: RecyclerView.ItemDecoration?
) {
    adapter?.let { recyclerView.adapter = it }

    listener?.let { (recyclerView.adapter as? BaseRecyclerViewAdapter<*>)?.onItemClickListener = it }

    longListener?.let { (recyclerView.adapter as? BaseRecyclerViewAdapter<*>)?.onItemLongClickListener = it }

    decor?.let { recyclerView.addItemDecoration(decor) }
}