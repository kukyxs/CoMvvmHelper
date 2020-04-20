@file:Suppress("MemberVisibilityCanBePrivate")

package com.kuky.android.comvvmhelper.ui

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.*
import com.kuky.android.comvvmhelper.listener.OnItemClickListener
import com.kuky.android.comvvmhelper.listener.OnItemLongClickListener


/**
 * @author kuky.
 * @description
 */

private const val HEADER = 100000

private const val FOOTER = 200000

open class BaseViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

/**
 * DiffUtilCallBack 基类
 */
abstract class BaseDiffCallback<T : Any>(newItems: MutableList<T>?) : DiffUtil.Callback() {
    private var newList = newItems
    var oldList: MutableList<T>? = null

    override fun getOldListSize(): Int = oldList?.size ?: 0

    override fun getNewListSize(): Int = newList?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        if (oldList.isNullOrEmpty() || newList.isNullOrEmpty()) false
        else areSameItems(oldList!![oldItemPosition], newList!![newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        if (oldList.isNullOrEmpty() || newList.isNullOrEmpty()) false
        else areSameContent(oldList!![oldItemPosition], newList!![newItemPosition])

    abstract fun areSameItems(old: T, new: T): Boolean

    abstract fun areSameContent(old: T, new: T): Boolean

    fun getNewItems() = newList
}

/**
 * 用于修改刷新机制
 */
class BaseListUpdateCallback<T : Any>(private val adapter: BaseRecyclerViewAdapter<T>) :
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

abstract class BaseRecyclerViewAdapter<T : Any>(dataList: MutableList<T>? = null) :
    RecyclerView.Adapter<BaseViewHolder>() {

    protected var mDataList = dataList
    private val mHeaderViewList = SparseArray<ViewDataBinding>()
    private val mFooterViewList = SparseArray<ViewDataBinding>()

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null

    fun updateAdapterDataList(dataList: MutableList<T>?) {
        mDataList = dataList
        notifyDataSetChanged()
    }

    /**
     * 刷新数据，使用 diffutil
     */
    fun updateAdapterDataList(helper: BaseDiffCallback<T>) {
        helper.oldList = getAdapterDataList()
        val result = DiffUtil.calculateDiff(helper, true)
        result.dispatchUpdatesTo(BaseListUpdateCallback(this))
        mDataList = helper.getNewItems()
    }

    /**
     * 头部添加数据
     */
    fun appendDataAtHead(dataList: MutableList<T>) {
        mDataList = (mDataList ?: arrayListOf()).apply { addAll(0, dataList) }
        notifyItemRangeInserted(getHeaderSize(), dataList.size)
    }

    /**
     * 尾部添加数据
     */
    fun appendDataAtTails(dataList: MutableList<T>) {
        val rangeStar = getDataSize()
        mDataList = (mDataList ?: arrayListOf()).apply { addAll(dataList) }
        notifyItemRangeInserted(getHeaderSize() + rangeStar, dataList.size)
    }

    /**
     * 移除某位的数据
     */
    fun removeDataAtPosition(position: Int) {
        mDataList?.let {
            it.removeAt(position)
            val realPosition = position + getHeaderSize()
            notifyItemRemoved(realPosition)

            if (realPosition != getDataSize() + getHeaderSize())
                notifyItemRangeChanged(realPosition, getDataSize() + getHeaderSize() - realPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        if (haveHeader() && mHeaderViewList.get(viewType) != null)
            BaseViewHolder(mHeaderViewList.get(viewType))
        else if (haveFooter() && mFooterViewList.get(viewType) != null)
            BaseViewHolder(mFooterViewList.get(viewType))
        else
            BaseViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId(viewType), parent, false))

    abstract fun layoutId(viewType: Int): Int

    override fun getItemCount() = getHeaderSize() + getDataSize() + getFooterSize()

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (!isHeader(position) && !isFooter(position)) {
            val realDataPosition = position - getHeaderSize()
            val data = mDataList?.get(realDataPosition) ?: return

            setVariable(data, realDataPosition, holder)
            holder.binding.executePendingBindings()

            holder.binding.root.let {
                it.setOnClickListener { v ->
                    onItemClickListener?.onItemClick(realDataPosition, v)
                }

                it.setOnLongClickListener { v ->
                    onItemLongClickListener?.onItemLongClick(realDataPosition, v) ?: false
                }
            }
        }
    }

    abstract fun setVariable(data: T, realDataPosition: Int, holder: BaseViewHolder)

    override fun getItemViewType(position: Int) =
        when {
            isHeader(position) -> mHeaderViewList.keyAt(position)
            isFooter(position) -> mFooterViewList.keyAt(position - getDataSize() - getHeaderSize())
            else -> getAdapterItemViewType(position)
        }

    open fun getAdapterItemViewType(position: Int): Int = 0

    fun addHeaderView(header: ViewDataBinding) {
        val headKey = HEADER + getHeaderSize()
        mHeaderViewList.put(headKey, header)
        notifyItemInserted(getHeaderSize())
    }

    fun addFooterView(footer: ViewDataBinding) {
        val footKey = FOOTER + getFooterSize()
        mFooterViewList.put(footKey, footer)
        notifyItemInserted(getHeaderSize() + getDataSize() + getFooterSize())
    }

    fun getAdapterDataList(): MutableList<T>? = mDataList

    fun getItemData(position: Int): T? = mDataList?.get(position)

    fun getHeaderSize(): Int = mHeaderViewList.size()

    fun getDataSize(): Int = mDataList?.size ?: 0

    fun getFooterSize(): Int = mFooterViewList.size()

    private fun haveHeader() = mHeaderViewList.size() > 0

    private fun haveFooter() = mFooterViewList.size() > 0

    private fun isHeader(pos: Int) = haveHeader() && pos < getHeaderSize()

    private fun isFooter(pos: Int) = haveFooter() && pos >= getHeaderSize() + getDataSize()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.layoutManager.let {
            if (it is GridLayoutManager)
                it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) =
                        if (isHeader(position) || isFooter(position)) it.spanCount
                        else 1
                }
        }
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.layoutParams.let {
            if (it is StaggeredGridLayoutManager.LayoutParams)
                it.isFullSpan = isHeader(holder.layoutPosition) || isFooter(holder.layoutPosition)
        }
    }
}