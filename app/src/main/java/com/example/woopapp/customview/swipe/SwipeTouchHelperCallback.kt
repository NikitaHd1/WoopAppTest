package com.example.woopapp.customview.swipe

import android.graphics.Canvas
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.woopapp.adapters.SwipeableCardsAdapter
import kotlin.math.abs

class SwipeTouchHelperCallback<T>(
    private val adapter: SwipeableCardsAdapter<RecyclerView.ViewHolder>,
    private val dataList: MutableList<T>,
    private var mListener: OnSwipeListener<T>? = null
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) = makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        viewHolder.itemView.setOnTouchListener(null)

        val layoutPosition = viewHolder.layoutPosition
        val remove: T = dataList[layoutPosition]
        mListener?.onSwiped(
            remove,
            direction
        )
        dataList.removeAt(layoutPosition)
        adapter.notifyDataSetChanged()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val ratio = getRatio(
            recyclerView.width,
            dX,
            viewHolder
        )

        itemView.rotation = ratio * DEFAULT_ROTATE_DEGREE
        val view = recyclerView.getChildAt(recyclerView.childCount - 1)
        view.translationY =
            -0.5f * abs(ratio) * itemView.measuredHeight / DEFAULT_TRANSLATE_Y

        if (ratio != 0f) {
            mListener?.onSwiping(
                ratio,
                if (ratio < 0) ItemTouchHelper.LEFT else ItemTouchHelper.RIGHT
            )
        }
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        viewHolder?.let {
            val ratio = getRatio(recyclerView.width, dX, it)
            val childCount = recyclerView.childCount

            adapter.hideShadow(viewHolder)

            if (childCount >= 2) {
                adapter.showShadow(recyclerView, viewHolder, ratio)
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.rotation = 0f
    }

    private fun getRatio(width: Int, dX: Float, viewHolder: RecyclerView.ViewHolder): Float {
        var ratio = dX / (width * getSwipeThreshold(viewHolder))
        if (ratio > 1) {
            ratio = 1f
        } else if (ratio < -1) {
            ratio = -1f
        }
        return ratio
    }

    companion object {

        private const val DEFAULT_ROTATE_DEGREE: Float = 12f

        const val DEFAULT_TRANSLATE_Y = 28
    }
}