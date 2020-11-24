package com.kk.android.comvvmhelper.ui

import androidx.recyclerview.widget.ListUpdateCallback

/**
 * @author kuky.
 * @description only worked for [BaseRecyclerViewAdapter]
 */
open class BaseListUpdateCallback<T : Any>(private val adapter: BaseRecyclerViewAdapter<T>) :
    ListUpdateCallback {

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter.notifyItemRangeChanged(position + adapter.getHeaderSize(), count, payload)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(fromPosition + adapter.getHeaderSize(), toPosition + adapter.getHeaderSize())
    }

    override fun onInserted(position: Int, count: Int) {
        adapter.notifyItemRangeInserted(position + adapter.getHeaderSize(), count)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter.notifyItemRangeRemoved(position + adapter.getHeaderSize(), count)
    }
}