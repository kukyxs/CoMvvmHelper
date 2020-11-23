package com.kk.android.comvvmhelper.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

/**
 * @author kuky.
 * @description only for GridLayoutManager
 */
class GridLayoutDivideItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }

    private var mDivider: Drawable?

    init {
        context.obtainStyledAttributes(ATTRS).run {
            mDivider = getDrawable(0)
            recycle()
        }
    }

    fun setDrawable(drawable: Drawable?) {
        if (drawable == null) return
        mDivider = drawable
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawVertical(c, parent)
        drawHorizontal(c, parent)
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val left = child.left + params.leftMargin
            val top = child.bottom + params.bottomMargin
            val right = child.right + params.rightMargin + (mDivider?.intrinsicWidth ?: 0)
            val bottom = top + (mDivider?.intrinsicHeight ?: 0)
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(c)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val left = child.right + params.rightMargin
            val top = child.top - params.topMargin
            val right = left + (mDivider?.intrinsicWidth ?: 0)
            val bottom = child.bottom + params.bottomMargin
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(c)
        }
    }

    private fun spanCount(parent: RecyclerView) = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: -1

    private fun isLastRow(itemIndex: Int, parent: RecyclerView): Boolean {
        (parent.layoutManager as? GridLayoutManager)?.let {
            val childCount = parent.adapter?.itemCount ?: 0
            val spanCount = spanCount(parent)
            val total = ceil(childCount.toDouble() / spanCount)
            val current = ceil((itemIndex + spanCount).toDouble() / spanCount)
            return current >= total
        }
        return true
    }

    private fun isLastColumn(itemIndex: Int, parent: RecyclerView): Boolean {
        (parent.layoutManager as? GridLayoutManager)?.let {
            val spanCount = spanCount(parent)
            return itemIndex % spanCount < spanCount
        }
        return false
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        if (isLastRow(parent.getChildAdapterPosition(view), parent)) {
            outRect.set(0, 0, 0, mDivider?.intrinsicHeight ?: 0)
        }

        if (isLastColumn(parent.getChildLayoutPosition(view), parent)) {
            outRect.set(0, 0, mDivider?.intrinsicWidth ?: 0, 0)
        }
    }
}