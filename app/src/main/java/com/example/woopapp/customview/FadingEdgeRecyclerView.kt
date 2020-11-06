package com.example.woopapp.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.woopapp.R
import kotlin.math.min

class FadingEdgeRecyclerView : RecyclerView {

    private var gradientSize = 0
    private var originalGradientSize = 0
    private var gradientPaint: Paint? = null
    private var gradientRectBottom: Rect? = null

    constructor(context: Context) : super(context) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        val defaultSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            DEFAULT_GRADIENT_SIZE_DP,
            resources.displayMetrics
        ).toInt()
        gradientSize = defaultSize
        attrs?.let {
            val attributeValues =
                context.obtainStyledAttributes(
                    it,
                    R.styleable.FadingEdgeRecyclerView
                )
            with(attributeValues) {
                try {
                    gradientSize = getDimensionPixelSize(
                        R.styleable.FadingEdgeRecyclerView_fadingEdgeLength,
                        defaultSize
                    )
                } finally {
                    recycle()
                }
            }
        }
        originalGradientSize = gradientSize

        gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        gradientPaint?.apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }
        gradientRectBottom = Rect()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        if (visibility == View.GONE || width == 0 || height == 0) {
            super.dispatchDraw(canvas)
            return
        }

        initBottomGradient()

        val count = canvas?.saveLayer(
            RectF(
                0.0f,
                0.0f,
                width.toFloat(),
                height.toFloat()
            ),
            null
        )
        super.dispatchDraw(canvas)

        if (gradientSize > 0 && gradientRectBottom != null && gradientPaint != null) {
            canvas?.drawRect(gradientRectBottom!!, gradientPaint!!)
        }

        count?.let { canvas.restoreToCount(it) }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        val offset = computeVerticalScrollOffset()
        val extent = computeVerticalScrollExtent()
        val range = computeVerticalScrollRange()

        val percentage = (100 * offset / (range - extent))
        setFadeSizeInPercentage(percentage)
    }

    private fun setFadeSizeInPercentage(percentage: Int) {
        if (percentage > 0) {
            val newGradientSize = originalGradientSize * (100 - percentage) / 100
            if (gradientSize != newGradientSize) {
                gradientSize = newGradientSize
                invalidate()
            }
        }
    }

    private fun initBottomGradient() {
        val actualHeight = height - paddingTop - paddingBottom
        val size = min(gradientSize, actualHeight)
        val left = paddingLeft
        val top = paddingTop + actualHeight - size
        val right = width - paddingRight
        val bottom = top + size
        gradientRectBottom?.set(left, top, right, bottom)
        val gradient = LinearGradient(
            left.toFloat(),
            top.toFloat(),
            left.toFloat(),
            bottom.toFloat(),
            DEFAULT_GRADIENT_COLORS,
            null,
            Shader.TileMode.CLAMP
        )
        gradientPaint?.shader = gradient
    }

    companion object {

        private const val DEFAULT_GRADIENT_SIZE_DP = 80f

        private val DEFAULT_GRADIENT_COLORS = intArrayOf(Color.WHITE, Color.TRANSPARENT)
    }
}