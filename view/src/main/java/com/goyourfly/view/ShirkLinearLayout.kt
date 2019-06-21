package com.goyourfly.view

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * ShirkLinearLayout 是一个自定 LinearLayout
 * 它的目的是当 LinearLayout 的子 View 高度超过
 * LinearLayout 的高度时，让子 View 按照一定的比
 * 例缩放，同时如果子 View
 */
class ShirkLinearLayout : LinearLayout {
    companion object {
        val TAG = "ShirkLinearLayout"
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        val childrenSize = getChildrenSizeWithMargin(widthMeasureSpec, heightMeasureSpec)
        val overflowSize = if (orientation == VERTICAL) childrenSize - heightSize else childrenSize - widthSize
        val shirkSum = getShirkSum()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams
            lp.resetToOriginSize(orientation)
        }
        if (overflowSize > 0) {
            tryShirk(overflowSize, shirkSum)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun tryShirk(overflowSize: Int, shirtSum: Float) {
        var overflowSizeTemp = overflowSize
        var shirkSumTemp = shirtSum
        var allZero = true
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams
            val lpSize = getLpSize(lp)
            val lpMarginStart = getLpMarginStart(lp)
            val lpMarginEnd = getLpMarginEnd(lp)
            if (lpSize == 0) {
                continue
            }
            allZero = false
            val shirk = lp.shirk
            if (shirk > 0) {
                if (lpSize < 0) {
                    val size = getSize(child)
                    setLpSize(lp, size)
                }
                val percent = shirk / shirkSumTemp
                var shirkSize = Math.ceil((percent * overflowSize).toDouble()).toInt()
                // - Bottom margin
                if (shirkSize > 0) {
                    val s = Math.min(lpMarginEnd, shirkSize)
                    Log.d(TAG, "ShirkSize:$shirkSize,SSS:$s")
                    setLpMarginEnd(lp, lpMarginEnd - s)
                    shirkSize -= s
                    overflowSizeTemp -= s
                }
                // - Top margin
                if (shirkSize > 0) {
                    val s = Math.min(lpMarginStart, shirkSize)
                    setLpMarginStart(lp, Math.max(0, lpMarginStart - s))
                    shirkSize -= s
                    overflowSizeTemp -= s
                }
                // -
                if (shirkSize > 0) {
                    val s = Math.min(lpSize, shirkSize)
                    setLpSize(lp, lpSize - s)
                    shirkSize -= s
                    overflowSizeTemp -= s
                }
                if(shirkSize > 0){
                    shirkSumTemp -= shirk
                }
            }
        }
        if (!allZero && overflowSizeTemp > 0) {
            tryShirk(overflowSizeTemp, shirkSumTemp)
        }
    }

    private fun getSize(child: View): Int {
        return if (orientation == VERTICAL) child.measuredHeight else child.measuredWidth;
    }

    private fun getLpSize(lp: LinearLayout.LayoutParams): Int {
        return if (orientation == VERTICAL) lp.height else lp.width;
    }

    private fun getLpMarginStart(lp: LinearLayout.LayoutParams): Int {
        return if (orientation == VERTICAL) lp.topMargin else lp.leftMargin;
    }

    private fun getLpMarginEnd(lp: LinearLayout.LayoutParams): Int {
        return if (orientation == VERTICAL) lp.bottomMargin else lp.rightMargin;
    }

    private fun setLpSize(lp: LinearLayout.LayoutParams, size: Int) {
        if (orientation == VERTICAL) lp.height = size else lp.width = size
    }

    private fun setLpMarginStart(lp: LinearLayout.LayoutParams, size: Int) {
        if (orientation == VERTICAL) lp.topMargin = size else lp.leftMargin = size
    }

    private fun setLpMarginEnd(lp: LinearLayout.LayoutParams, size: Int) {
        if (orientation == VERTICAL) lp.bottomMargin = size else lp.rightMargin = size
    }

    private fun getChildrenSizeWithMargin(widthMeasureSpec: Int, heightMeasureSpec: Int): Int {
        var size = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams
            if (lp.maxSize >= 0) {
                size += lp.maxSize
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
                val childSize = if (orientation == VERTICAL) {
                    lp.originSize = lp.height
                    lp.originMarginStart = lp.topMargin
                    lp.originMarginEnd = lp.bottomMargin
                    child.measuredHeight + lp.topMargin + lp.bottomMargin
                } else {
                    lp.originSize = lp.width
                    lp.originMarginStart = lp.leftMargin
                    lp.originMarginEnd = lp.rightMargin
                    child.measuredWidth + lp.leftMargin + lp.rightMargin
                }
                lp.maxSize = childSize
                size += childSize
            }
        }
        return size;
    }

    private fun getShirkSum(): Float {
        return (0 until childCount).map {
            val child = getChildAt(it)
            return@map child
        }.sumByDouble {
            val lp = it.layoutParams as LayoutParams
            return@sumByDouble lp.shirk.toDouble()
        }.toFloat()
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams? {
        if (orientation == HORIZONTAL) {
            return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        } else if (orientation == VERTICAL) {
            return LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
        return null
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): LayoutParams {
        return LayoutParams(lp)
    }


    class LayoutParams : LinearLayout.LayoutParams {
        var shirk = 0F
        var maxSize = -1;
        var originSize = -1;
        var originMarginStart = -1;
        var originMarginEnd = -1;

        fun resetToOriginSize(orientation: Int) {
            if (orientation == VERTICAL) {
                height = originSize
                topMargin = originMarginStart
                bottomMargin = originMarginEnd
            } else {
                width = originSize
                leftMargin = originMarginStart
                rightMargin = originMarginEnd
            }
        }

        constructor (c: Context, attrs: AttributeSet) : super(c, attrs) {
            c.obtainStyledAttributes(attrs, R.styleable.ShirkLinearLayout_Layout).apply {
                try {
                    shirk = getFloat(R.styleable.ShirkLinearLayout_Layout_shirk, 0F)
                } finally {
                    recycle()
                }
            };
        }

        constructor(width: Int, height: Int) : super(width, height)

        constructor(width: Int, height: Int, weight: Float) : super(width, height, weight)

        constructor(p: ViewGroup.LayoutParams) : super(p)

        constructor(p: ViewGroup.MarginLayoutParams) : super(p)

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        constructor(source: LayoutParams) : super(source)

        companion object {
            /**
             * Special value for the height or width requested by a View.
             * FILL_PARENT means that the view wants to be as big as its parent,
             * minus the parent's padding, if any. This value is deprecated
             * starting in API Level 8 and replaced by [.MATCH_PARENT].
             */
            @Deprecated("")
            val FILL_PARENT = -1

            /**
             * Special value for the height or width requested by a View.
             * MATCH_PARENT means that the view wants to be as big as its parent,
             * minus the parent's padding, if any. Introduced in API Level 8.
             */
            val MATCH_PARENT = -1

            /**
             * Special value for the height or width requested by a View.
             * WRAP_CONTENT means that the view wants to be just large enough to fit
             * its own internal content, taking its own padding into account.
             */
            val WRAP_CONTENT = -2
        }
    }
}