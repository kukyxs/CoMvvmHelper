package com.kk.android.comvvmhelper.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kk.android.comvvmhelper.listener.OnItemClickListener
import com.kk.android.comvvmhelper.listener.OnItemLongClickListener
import com.kk.android.comvvmhelper.ui.BaseRecyclerViewAdapter

/**
 * 绑定 RecyclerView 的点击事件
 * @param listener 点击事件，[OnItemClickListener]
 */

@BindingAdapter(value = ["bind:recyclerAdapter", "bind:onRecyclerItemClick", "bind:onRecyclerItemLongClick"], requireAll = false)
fun bindRecyclerAdapter(
    recyclerView: RecyclerView, adapter: BaseRecyclerViewAdapter<*>?,
    listener: OnItemClickListener?,
    longListener: OnItemLongClickListener?
) {
    adapter?.let { recyclerView.adapter = it }

    listener?.let { (recyclerView.adapter as? BaseRecyclerViewAdapter<*>)?.onItemClickListener = it }

    longListener?.let { (recyclerView.adapter as? BaseRecyclerViewAdapter<*>)?.onItemLongClickListener = it }
}

/**
 * 绑定 recyclerView 分割线
 */
@BindingAdapter("bind:recyclerDivider")
fun bindRecyclerDivider(recyclerView: RecyclerView, decor: RecyclerView.ItemDecoration) {
    recyclerView.addItemDecoration(decor)
}