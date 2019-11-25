package com.kuky.android.comvvmhelper.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kuky.android.comvvmhelper.listener.OnItemClickListener
import com.kuky.android.comvvmhelper.listener.OnItemLongClickListener
import com.kuky.android.comvvmhelper.ui.BaseRecyclerViewAdapter

/**
 * 绑定 RecyclerView 的点击事件
 * @param listener 点击事件，[OnItemClickListener]
 */
@BindingAdapter("bind:listItemClick")
fun bindRecyclerItemClick(recyclerView: RecyclerView, listener: OnItemClickListener?) {
    val adapter = recyclerView.adapter

    if (adapter == null || adapter !is BaseRecyclerViewAdapter<*, *>) return

    adapter.onItemClickListener = listener
}

/**
 * 绑定 RecyclerView 的长按事件
 * @param listener 点击事件，[OnItemLongClickListener]
 */
@BindingAdapter("bind:listItemLongClick")
fun bindRecyclerItemLOngClick(recyclerView: RecyclerView, listener: OnItemLongClickListener?) {
    val adapter = recyclerView.adapter

    if (adapter == null || adapter !is BaseRecyclerViewAdapter<*, *>) return

    adapter.onItemLongClickListener = listener
}

/**
 * 绑定 recyclerView 分割线
 */
@BindingAdapter("bind:divider")
fun bindRecyclerDivider(recyclerView: RecyclerView, decor: RecyclerView.ItemDecoration) {
    recyclerView.addItemDecoration(decor)
}

/**
 * recyclerView 是否固定高度
 */
@BindingAdapter("bind:hasFixedSize")
fun bindRecyclerHasFixedSize(recyclerView: RecyclerView, hasFixedSize: Boolean) {
    recyclerView.setHasFixedSize(hasFixedSize)
}

/**
 * recyclerView 滚动到指定 position，并指定偏移量
 */
@BindingAdapter(value = ["bind:scrollTo", "bind:offset"])
fun bindScrollTo(recyclerView: RecyclerView, position: Int, offset: Int) {
    recyclerView.layoutManager.let {
        when (it) {
            is LinearLayoutManager -> it.scrollToPositionWithOffset(position, offset)

            is GridLayoutManager -> it.scrollToPositionWithOffset(position, offset)

            is StaggeredGridLayoutManager -> it.scrollToPositionWithOffset(position, offset)
        }
    }
}

@BindingAdapter("bind:scrollListener")
fun bindRecyclerScrollListener(recyclerView: RecyclerView, l: RecyclerView.OnScrollListener) {
    recyclerView.addOnScrollListener(l)
}