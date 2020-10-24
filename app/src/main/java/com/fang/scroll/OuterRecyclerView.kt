package com.fang.scroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.OverScroller
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * @author fangkw on 2020-10-23
 **/
class OuterRecyclerView(context: Context, attributeSet: AttributeSet) : RecyclerView(context, attributeSet) {

    private val mParentScrollConsumed = IntArray(2)


    private var mCurrentFling = 0
    private val overScroller = OverScroller(context, Interpolator {
        val t = it - 1.0f
        t * t * t * t * t + 1.0f
    })

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.actionMasked == MotionEvent.ACTION_CANCEL) {
            Log.e("fang", "cancel")
        }

        if (ev?.actionMasked == MotionEvent.ACTION_DOWN) {
            if (!overScroller.isFinished) {
                overScroller.abortAnimation()
            }
        }
        val ns = super.dispatchTouchEvent(ev)
        Log.e("fang", "dispatchTouchEvent-->$ns")
        return ns
    }

    /**
     * if (actionMasked == MotionEvent.ACTION_DOWN
     * || mFirstTouchTarget != null) {
     * final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
     * if (!disallowIntercept) {
     *      intercepted = onInterceptTouchEvent(ev);
     *      ev.setAction(action); // restore action in case it was changed
     *   } else {
     *      intercepted = false;
     *   }
     * }
     *
     * onInterceptTouchEvent 执行的条件 actionMasked == MotionEvent.ACTION_DOWN 或者 mFirstTouchTarget != null
     * mFirstTouchTarget != null 的意思是 有child可以处理事件
     * */
    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        val ns = super.onInterceptTouchEvent(e)
        Log.e("fang", "onInterceptTouchEvent-->$ns")
        return ns
    }

    /**
     * =======================================================
     * NestedScrollingChild2 start
     * =======================================================
     * */
    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        var consumedSelf = false
        if (type == ViewCompat.TYPE_TOUCH) {
            // up
            if (dy > 0) {
                if (!canScrollVertically(1)) {
                    val target = fetchNestedChild()
                    target?.apply {
                        this.scrollBy(0, dy)

                        consumed?.let {
                            it[1] = dy
                        }

                        consumedSelf = true
                    }
                }
            }
            // down
            if (dy < 0) {
                val target = fetchNestedChild()
                target?.apply {
                    if (this.canScrollVertically(-1)) {
                        this.scrollBy(0, dy)

                        consumed?.let {
                            it[1] = dy
                        }

                        consumedSelf = true
                    }
                }
            }
        }

        // Now let our nested parent consume the leftovers
        val parentScrollConsumed = mParentScrollConsumed
        val parentConsumed = super.dispatchNestedPreScroll(dx, dy - (consumed?.get(1)?:0), parentScrollConsumed, offsetInWindow, type)
        consumed?.let {
            consumed[1] += parentScrollConsumed[1]
        }
        return consumedSelf || parentConsumed
    }

    /**
     * fling 回调是一次性的 无法同时分发到两个View
     * 只能自己托管fling
     * */
    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        fling(velocityY)
        return true
    }
    /**
     * =======================================================
     * NestedScrollingChild2 end
     * =======================================================
     * */

    /**
     * 托管fling
     * 利用OverScroller
     * */
    private fun fling(velocityY: Float) {
        mCurrentFling = 0
        overScroller.fling(0, 0, 0, velocityY.toInt(), 0, 0, Int.MIN_VALUE, Int.MAX_VALUE)
        invalidate()
    }

    private fun fetchNestedChild() : View? {
        val view = getChildAt(childCount-1)
        if (view is TabLinearLayout) {
            return view.innerRecyclerView
        }
        return null
    }

    override fun computeScroll() {
        if (overScroller.computeScrollOffset()) {
            val current = overScroller.currY
            val dy = current - mCurrentFling
            mCurrentFling = current

            val target = fetchNestedChild()
            if (dy > 0) {
                if (canScrollVertically(1)) {
                    scrollBy(0, dy)
                } else {
                    if (target?.canScrollVertically(1) == true) {
                        target.scrollBy(0, dy)
                    } else {
                        if (!overScroller.isFinished) {
                            overScroller.abortAnimation()
                        }
                    }
                }
            }
            if (dy < 0) {
                if (target?.canScrollVertically(-1) == true) {
                    target.scrollBy(0, dy)
                } else {
                    if (canScrollVertically(-1)) {
                        scrollBy(0, dy)
                    } else {
                        if (!overScroller.isFinished) {
                            overScroller.abortAnimation()
                        }
                    }
                }
            }
            invalidate()
        }
        super.computeScroll()
    }
}