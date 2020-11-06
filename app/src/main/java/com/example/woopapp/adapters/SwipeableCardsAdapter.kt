package com.example.woopapp.adapters

import androidx.recyclerview.widget.RecyclerView

abstract class SwipeableCardsAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    abstract fun showShadow(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        ratio: Float
    )

    abstract fun hideShadow(viewHolder: RecyclerView.ViewHolder)
}