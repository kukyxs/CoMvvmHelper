package com.kk.android.comvvmhelper.widget

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.yes
import kotlin.math.abs
import kotlin.math.floor

/**
 * @author kuky.
 * @description
 */
class StacksLayoutManager(gap: Float = 0f, orientation: Int = HORIZONTAL) : RecyclerView.LayoutManager() {

    companion object {
        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL
        private const val MIN_SCALE = 0.6f
        private const val SCROLL_DURATION = 150L
    }

    private var mCompletedItemSize = -1f

    private var mFirstItemTotalScrollOffset = -1f

    private var mFirstVisiblePosition = 0

    private var mLastVisiblePosition = 0

    private var mHorizontalOffset = 0L

    private var mVerticalOffset = 0L

    private var mItemGap = gap

    private var mOrientation = orientation

    private var mChildWidth = 0

    private var mChildHeight = 0

    private var mScrollAnim: ValueAnimator? = null

    override fun generateDefaultLayoutParams() = RecyclerView.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            return
        }

        mCompletedItemSize = -1f
        detachAndScrapAttachedViews(recycler)

        (mOrientation == HORIZONTAL).yes { fillHorizontal(recycler, state, 0) }
            .otherwise { fillVertical(recycler, state, 0) }
    }

    override fun canScrollHorizontally() = mOrientation == HORIZONTAL

    override fun canScrollVertically() = mOrientation == VERTICAL

    private fun fillHorizontal(recycler: RecyclerView.Recycler, state: RecyclerView.State, dx: Int): Int {
        val result = fillHorizontalLeft(recycler, state, dx)
        recycleChildren(recycler)
        return result
    }

    private fun fillVertical(recycler: RecyclerView.Recycler, state: RecyclerView.State, dy: Int): Int {
        val result = fillVerticalTop(recycler, state, dy)
        recycleChildren(recycler)
        return result
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        return (dx == 0 || childCount == 0 || abs(dx / 1f) < 0.00000001f).yes { 0 }
            .otherwise {
                mHorizontalOffset += dx
                fillHorizontal(recycler, state, dx)
            }
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        return (dy == 0 || childCount == 0 || abs(dy / 1f) < 0.00000001f).yes { 0 }
            .otherwise {
                mVerticalOffset += dy
                fillVertical(recycler, state, dy)
            }
    }

    private fun getMaxHorizontalOffset(): Float {
        return (mChildWidth == 0 || itemCount == 0).yes { 0f }.otherwise {
            (mChildWidth + mItemGap) * (itemCount - 1)
        }
    }

    private fun getMinHorizontalOffset(): Float {
        return (mChildWidth == 0).yes { 0f }.otherwise { (width - mChildWidth) / 2f }
    }

    private fun getMaxVerticalOffset(): Float {
        return (mChildHeight == 0 || itemCount == 0).yes { 0f }.otherwise {
            (mChildHeight + mItemGap) * (itemCount - 1)
        }
    }

    private fun getMinVerticalOffset(): Float {
        return (mChildHeight == 0).yes { 0f }.otherwise { (height - mChildHeight) / 2f }
    }

    private fun fillHorizontalLeft(recycler: RecyclerView.Recycler, state: RecyclerView.State, dx: Int): Int {
        var result = dx

        if (dx < 0) {
            if (mHorizontalOffset < 0) {
                mHorizontalOffset = 0
                result = 0
            }
        }

        if (dx > 0) {
            if (mHorizontalOffset >= getMaxHorizontalOffset()) {
                mHorizontalOffset = getMaxHorizontalOffset().toLong()
                result = 0
            }
        }

        // 分离并加入到临时缓存
        try {
            detachAndScrapAttachedViews(recycler)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var tempView: View? = null
        var tempPosition = -1

        if (mCompletedItemSize == -1f) {
            tempPosition = mFirstVisiblePosition
            tempView = recycler.getViewForPosition(tempPosition)
            measureChildWithMargins(tempView, 0, 0)
            mChildWidth = getDecoratedMeasureHorizontal(tempView)
        }

        mFirstItemTotalScrollOffset = (width + mChildWidth) / 2f

        var startX: Float
        val fraction: Float

        if (mHorizontalOffset >= mFirstItemTotalScrollOffset) {
            startX = mItemGap
            mCompletedItemSize = mChildWidth + mItemGap
            mFirstVisiblePosition = (floor(abs(mHorizontalOffset - mFirstItemTotalScrollOffset) / mCompletedItemSize) + 1).toInt()
            fraction = (abs(mHorizontalOffset - mFirstItemTotalScrollOffset) % mCompletedItemSize) / mCompletedItemSize
        } else {
            mFirstVisiblePosition = 0
            startX = getMinHorizontalOffset()
            mCompletedItemSize = mFirstItemTotalScrollOffset
            fraction = (abs(mHorizontalOffset) % mCompletedItemSize) / mCompletedItemSize
        }

        mLastVisiblePosition = itemCount - 1

        val normalViewOffset = mCompletedItemSize * fraction
        var isNormalViewOffsetSet = false

        for (i in mFirstVisiblePosition..mLastVisiblePosition) {
            val item = (tempPosition == i && tempView != null).yes { tempView!! }
                .otherwise { recycler.getViewForPosition(i) }

            val focus = (abs(mHorizontalOffset) / (mChildWidth + mItemGap)).toInt()

            (i < focus).yes { addView(item) }.otherwise { addView(item, 0) }

            measureChildWithMargins(item, 0, 0)

            if (!isNormalViewOffsetSet) {
                startX -= normalViewOffset
                isNormalViewOffsetSet = true
            }

            val left = startX.toInt()
            val top = paddingTop
            val right = left + getDecoratedMeasureHorizontal(item)
            val bottom = top + getDecoratedMeasureVertical(item)

            val childCenterX = (left + right) / 2
            val parentCenterX = width / 2
            val isChildLayoutLeft = childCenterX <= parentCenterX

            val fractionScale = isChildLayoutLeft.yes {
                (parentCenterX - childCenterX) / parentCenterX.toFloat()
            }.otherwise {
                (childCenterX - parentCenterX) / parentCenterX.toFloat()
            }
            val currentScale = 1f - (1f - MIN_SCALE) * fractionScale

            item.scaleX = currentScale
            item.scaleY = currentScale

            layoutDecoratedWithMargins(item, left, top, right, bottom)

            startX += (mChildWidth + mItemGap)

            if (startX > width - paddingRight) {
                mLastVisiblePosition = i
                break
            }
        }

        return result
    }

    private fun fillVerticalTop(recycler: RecyclerView.Recycler, state: RecyclerView.State, dy: Int): Int {
        var result = dy

        if (dy < 0) {
            if (mVerticalOffset < 0) {
                mVerticalOffset = 0
                result = 0
            }
        }

        if (dy > 0) {
            if (mVerticalOffset >= getMaxVerticalOffset()) {
                mVerticalOffset = getMaxVerticalOffset().toLong()
                result = 0
            }
        }

        // 分离并加入到临时缓存
        try {
            detachAndScrapAttachedViews(recycler)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var tempView: View? = null
        var tempPosition = -1

        if (mCompletedItemSize == -1f) {
            tempPosition = mFirstVisiblePosition
            tempView = recycler.getViewForPosition(tempPosition)
            measureChildWithMargins(tempView, 0, 0)
            mChildHeight = getDecoratedMeasureVertical(tempView)
        }

        mFirstItemTotalScrollOffset = (height + mChildHeight) / 2f

        var startY: Float
        val fraction: Float

        if (mVerticalOffset >= mFirstItemTotalScrollOffset) {
            startY = mItemGap
            mCompletedItemSize = mChildHeight + mItemGap
            mFirstVisiblePosition = (floor(abs(mVerticalOffset - mFirstItemTotalScrollOffset) / mCompletedItemSize) + 1).toInt()
            fraction = (abs(mVerticalOffset - mFirstItemTotalScrollOffset) % mCompletedItemSize) / mCompletedItemSize
        } else {
            mFirstVisiblePosition = 0
            startY = getMinVerticalOffset()
            mCompletedItemSize = mFirstItemTotalScrollOffset
            fraction = (abs(mVerticalOffset) % mCompletedItemSize) / mCompletedItemSize
        }

        mLastVisiblePosition = itemCount - 1

        val normalViewOffset = mCompletedItemSize * fraction
        var isNormalViewOffsetSet = false

        for (i in mFirstVisiblePosition..mLastVisiblePosition) {
            val item = (tempPosition == i && tempView != null).yes { tempView!! }
                .otherwise { recycler.getViewForPosition(i) }

            val focus = (abs(mVerticalOffset) / (mChildHeight + mItemGap)).toInt()

            (i < focus).yes { addView(item) }.otherwise { addView(item, 0) }

            measureChildWithMargins(item, 0, 0)

            if (!isNormalViewOffsetSet) {
                startY -= normalViewOffset
                isNormalViewOffsetSet = true
            }

            val left = paddingLeft
            val top = startY.toInt()
            val right = left + getDecoratedMeasureHorizontal(item)
            val bottom = top + getDecoratedMeasureVertical(item)

            val childCenterY = (top + bottom) / 2
            val parentCenterY = height / 2
            val isChildLayoutTop = childCenterY <= parentCenterY

            val fractionScale = isChildLayoutTop.yes {
                (parentCenterY - childCenterY) / parentCenterY.toFloat()
            }.otherwise {
                (childCenterY - parentCenterY) / parentCenterY.toFloat()
            }
            val currentScale = 1f - (1f - MIN_SCALE) * fractionScale

            item.scaleX = currentScale
            item.scaleY = currentScale

            layoutDecoratedWithMargins(item, left, top, right, bottom)

            startY += (mChildHeight + mItemGap)

            if (startY > height - paddingBottom) {
                mLastVisiblePosition = i
                break
            }
        }

        return result
    }

    private fun recycleChildren(recycler: RecyclerView.Recycler) {
        val list = recycler.scrapList
        val iterator = list.iterator()

        synchronized(list) {
            while (iterator.hasNext()) {
                removeAndRecycleView(iterator.next().itemView, recycler)
            }
        }
    }

    private fun getDecoratedMeasureHorizontal(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin
    }

    private fun getDecoratedMeasureVertical(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
            mScrollAnim?.let { cancelAnimator(it) }
        }
    }

    /**
     * 取消动画
     */
    private fun cancelAnimator(animator: ValueAnimator) {
        if (animator.isRunning || animator.isStarted) {
            animator.cancel()
        }
    }

    /**
     * 当前可见的第一项和最后一项
     */
    fun visiblePositions() = intArrayOf(mFirstVisiblePosition, mLastVisiblePosition)

    override fun scrollToPosition(position: Int) {
        val distance = getOffsetToTargetPosition(position)
        if (distance == 0f) return
        (mOrientation == HORIZONTAL).yes { horizontalSmoothScroll(distance) }
            .otherwise { verticalSmoothScroll(distance) }
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        throw IllegalAccessException("call scrollToPosition instead")
    }

    /**
     * 横向滚动
     */
    private fun horizontalSmoothScroll(distance: Float) {
        val startOffset = mHorizontalOffset
        val disFraction = abs(distance) / (mChildWidth + mItemGap)
        val duration = (abs(disFraction) < (mChildWidth + mItemGap)).yes {
            (SCROLL_DURATION * disFraction).coerceAtLeast(100f).toLong()
        }.otherwise { (SCROLL_DURATION * disFraction).toLong() }

        mScrollAnim = ValueAnimator.ofFloat(0f, distance).apply {
            setDuration(duration)
            addUpdateListener { anim ->
                val dis = anim.animatedValue as Float
                mHorizontalOffset = (startOffset + dis).toLong()
                requestLayout()
            }
        }
        mScrollAnim?.start()
    }

    /**
     * 垂直滚动
     */
    private fun verticalSmoothScroll(distance: Float) {
        val startOffset = mVerticalOffset
        val disFraction = abs(distance) / (mChildHeight + mItemGap)
        val duration = (abs(disFraction) < (mChildHeight + mItemGap)).yes {
            (SCROLL_DURATION * disFraction).coerceAtLeast(100f).toLong()
        }.otherwise { (SCROLL_DURATION * disFraction).toLong() }

        mScrollAnim = ValueAnimator.ofFloat(0f, distance).apply {
            setDuration(duration)
            addUpdateListener { anim ->
                val dis = anim.animatedValue as Float
                mVerticalOffset = (startOffset + dis).toLong()
                requestLayout()
            }
        }
        mScrollAnim?.start()
    }

    private fun getOffsetToTargetPosition(position: Int): Float {
        return (mOrientation == HORIZONTAL).yes { position * (mChildWidth + mItemGap) - abs(mHorizontalOffset) }
            .otherwise { position * (mChildHeight + mItemGap) - abs(mVerticalOffset) }
    }
}