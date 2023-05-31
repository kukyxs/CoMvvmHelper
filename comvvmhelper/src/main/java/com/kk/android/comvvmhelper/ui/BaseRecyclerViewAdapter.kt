@file:Suppress("MemberVisibilityCanBePrivate", "DEPRECATION")

package com.kk.android.comvvmhelper.ui

import android.annotation.SuppressLint
import android.util.SparseArray
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kk.android.comvvmhelper.helper.KLogger
import com.kk.android.comvvmhelper.helper.logs
import com.kk.android.comvvmhelper.listener.OnRecyclerItemClickListener
import com.kk.android.comvvmhelper.listener.OnRecyclerItemLongClickListener

/**
 * @author kuky.
 * @description
 */
abstract class BaseRecyclerViewAdapter<T : Any>(
    dataList: List<T>? = null
) : RecyclerView.Adapter<BaseRecyclerViewHolder>(), KLogger {

    companion object {
        private const val HEADER = 100_000

        private const val FOOTER = 200_000
    }

    protected var mDataList: MutableList<T> = checkDataNonnull(dataList)

    @Deprecated("use ConcatAdapter instead", level = DeprecationLevel.WARNING)
    private val mHeaderViewList = SparseArray<ViewDataBinding>()

    @Deprecated("use ConcatAdapter instead", level = DeprecationLevel.WARNING)
    private val mFooterViewList = SparseArray<ViewDataBinding>()

    var onItemClickListener: OnRecyclerItemClickListener? = null
    var onItemLongClickListener: OnRecyclerItemLongClickListener? = null

    private fun checkDataNonnull(dataList: List<T>?) =
        dataList.let { if (it.isNullOrEmpty()) listOf() else it }.toMutableList()

    @SuppressLint("NotifyDataSetChanged")
    open fun updateAdapterDataListWithoutAnim(dataList: List<T>?) {
        mDataList = checkDataNonnull(dataList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun updateAdapterDataListWithAnim(dataList: List<T>?) {
        if (mDataList == dataList) {
            logs("Update warning:", "update data source with the same List")
            notifyDataSetChanged()
            return
        }

        if (mDataList.isEmpty()) {
            mDataList = checkDataNonnull(dataList)
            notifyItemRangeInserted(0, mDataList.size)
            return
        }

        if (dataList.isNullOrEmpty()) {
            val count = mDataList.size
            mDataList = mutableListOf()
            notifyItemRangeRemoved(0, count)
            return
        }

        if (dataList.size > mDataList.size) {
            val start = mDataList.size - 1
            val range = dataList.size - mDataList.size
            mDataList = dataList.toMutableList()
            notifyItemRangeInserted(start, range)
        } else if (dataList.size == mDataList.size) {
            mDataList = dataList.toMutableList()
        } else {
            val range = mDataList.size - dataList.size
            mDataList = dataList.toMutableList()
            notifyItemRangeRemoved(dataList.size - 1, range)
        }
        notifyItemRangeChanged(0, mDataList.size)
    }

    /**
     * refresh data by diffutil
     */
    open fun updateAdapterDataListWithAnim(helper: BaseDiffCallback<T>) {
        helper.oldList = getAdapterDataList()
        val result = DiffUtil.calculateDiff(helper, true)
        result.dispatchUpdatesTo(BaseListUpdateCallback(this))
        mDataList = checkDataNonnull(helper.getNewItems())
    }

    /**
     * append data at head
     */
    fun appendDataAtHeadWithAnim(dataList: List<T>) {
        mDataList = mDataList.toMutableList().apply { addAll(0, dataList) }
        notifyItemRangeInserted(getHeaderSize(), dataList.size)
        notifyItemRangeChanged(getHeaderSize(), itemCount - dataList.size)
    }

    /**
     * append data at tail
     */
    fun appendDataAtTailWithAnim(dataList: List<T>) {
        val rangeStar = getDataSize()
        mDataList = mDataList.toMutableList().apply { addAll(dataList) }
        notifyItemRangeInserted(getHeaderSize() + rangeStar, dataList.size)
        notifyItemRangeChanged(getHeaderSize() + rangeStar, dataList.size)
    }

    /**
     * remove an item
     */
    fun removeDataAtPosition(position: Int) {
        mDataList.toMutableList().let {
            it.removeAt(position)
            val realPosition = position + getHeaderSize()
            notifyItemRemoved(realPosition)
            notifyItemRangeChanged(realPosition, itemCount - realPosition - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder =
        if (haveHeader() && mHeaderViewList.get(viewType) != null) {
            BaseRecyclerViewHolder(mHeaderViewList.get(viewType))
        } else if (haveFooter() && mFooterViewList.get(viewType) != null) {
            BaseRecyclerViewHolder(mFooterViewList.get(viewType))
        } else {
            BaseRecyclerViewHolder.createHolder(parent, layoutId(viewType))
        }

    abstract fun layoutId(viewType: Int): Int

    override fun getItemCount() = getHeaderSize() + getDataSize() + getFooterSize()

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
        if (!isHeader(position) && !isFooter(position)) {
            val dataPosition = position - getHeaderSize()
            val data = mDataList[dataPosition]

            setVariable(data, holder, dataPosition, position)
            holder.binding.executePendingBindings()

            holder.binding.root.let {
                it.setOnClickListener { v ->
                    onItemClickListener?.onRecyclerItemClick(dataPosition, v)
                }

                it.setOnLongClickListener { v ->
                    onItemLongClickListener?.onRecyclerItemLongClick(dataPosition, v) ?: false
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (!isHeader(position) && !isFooter(position)) {
                val dataPosition = position - getHeaderSize()
                val data = mDataList[dataPosition]
                setVariable(data, holder, dataPosition, position, payloads)
                holder.binding.executePendingBindings()

                holder.binding.root.let {
                    it.setOnClickListener { v ->
                        onItemClickListener?.onRecyclerItemClick(dataPosition, v)
                    }

                    it.setOnLongClickListener { v ->
                        onItemLongClickListener?.onRecyclerItemLongClick(dataPosition, v) ?: false
                    }
                }
            }
        }
    }

    open fun setVariable(data: T, holder: BaseRecyclerViewHolder, dataPosition: Int, layoutPosition: Int, payloads: MutableList<Any>) {
        setVariable(data, holder, dataPosition, layoutPosition)
    }

    abstract fun setVariable(data: T, holder: BaseRecyclerViewHolder, dataPosition: Int, layoutPosition: Int)

    override fun getItemViewType(position: Int) = when {
        isHeader(position) -> mHeaderViewList.keyAt(position)
        isFooter(position) -> mFooterViewList.keyAt(position - getDataSize() - getHeaderSize())
        else -> getAdapterItemViewType(position)
    }

    open fun getAdapterItemViewType(position: Int): Int = 0

    @Deprecated("use ConcatAdapter instead", level = DeprecationLevel.WARNING)
    fun addHeaderView(header: ViewDataBinding) {
        val headKey = HEADER + getHeaderSize()
        mHeaderViewList.put(headKey, header)
        notifyItemInserted(getHeaderSize() - 1)
        notifyItemRangeChanged(getHeaderSize() - 1, itemCount - getHeaderSize())
    }

    /**
     * 该方法目前使用需要按插入 header 顺序倒序移除，否则 notifyItemRangeChanged 会出现异常，目前未修复，请谨慎使用
     * @param header ViewDataBinding
     */
    @Deprecated("use ConcatAdapter instead", level = DeprecationLevel.WARNING)
    fun removeHeaderView(header: ViewDataBinding) {
        val index = mHeaderViewList.indexOfValue(header)
        mHeaderViewList.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount - index - 1)
    }

    @Deprecated("use ConcatAdapter instead", level = DeprecationLevel.WARNING)
    fun addFooterView(footer: ViewDataBinding) {
        val footKey = FOOTER + getFooterSize()
        mFooterViewList.put(footKey, footer)

        val insertIndex = getHeaderSize() + getDataSize() + getFooterSize() - 1
        notifyItemInserted(insertIndex)
        notifyItemRangeChanged(insertIndex, itemCount - insertIndex - 1)
    }

    @Deprecated("use ConcatAdapter instead", level = DeprecationLevel.WARNING)
    fun removeFooterView(footer: ViewDataBinding) {
        val index = mFooterViewList.indexOfValue(footer)
        mFooterViewList.removeAt(index)

        val removeIndex = getHeaderSize() + getDataSize() + index
        notifyItemRemoved(removeIndex)
        notifyItemRangeChanged(removeIndex, itemCount - removeIndex - 1)
    }

    fun getAdapterDataList(): MutableList<T> = mDataList.toMutableList()

    fun getItemData(position: Int): T? = if (position > mDataList.size) null else mDataList[position]

    @Deprecated("use ConcatAdapter instead", level = DeprecationLevel.WARNING)
    fun getHeaderSize(): Int = mHeaderViewList.size()

    fun getDataSize(): Int = mDataList.size

    @Deprecated("use ConcatAdapter instead", level = DeprecationLevel.WARNING)
    fun getFooterSize(): Int = mFooterViewList.size()

    @Deprecated("use ConcatAdapter instead", level = DeprecationLevel.WARNING)
    private fun haveHeader() = mHeaderViewList.size() > 0

    @Deprecated("use ConcatAdapter instead", level = DeprecationLevel.WARNING)
    private fun haveFooter() = mFooterViewList.size() > 0

    @Deprecated("use ConcatAdapter instead", level = DeprecationLevel.WARNING)
    private fun isHeader(pos: Int) = haveHeader() && pos < getHeaderSize()

    @Deprecated("use ConcatAdapter instead", level = DeprecationLevel.WARNING)
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

    override fun onViewAttachedToWindow(holder: BaseRecyclerViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.layoutParams.let {
            if (it is StaggeredGridLayoutManager.LayoutParams)
                it.isFullSpan = isHeader(holder.layoutPosition) || isFooter(holder.layoutPosition)
        }
    }
}