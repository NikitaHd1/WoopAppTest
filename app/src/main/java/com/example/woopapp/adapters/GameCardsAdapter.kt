package com.example.woopapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.woopapp.R
import com.example.woopapp.models.CardItem
import com.example.woopapp.util.ViewUtil.getProgressDrawable
import com.example.woopapp.util.ViewUtil.loadImageFromAsserts
import kotlinx.android.synthetic.main.game_card_item.view.*
import kotlin.math.abs

class GameCardsAdapter(private var gameCards: MutableList<CardItem>) :
    SwipeableCardsAdapter<GameCardsAdapter.CardsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_card_item, parent, false)
        return CardsViewHolder(view)
    }

    override fun getItemCount() = gameCards.size

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        holder.bind(gameCards[position])
    }

    override fun showShadow(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        ratio: Float
    ) {
        val childCount = recyclerView.childCount
        if (childCount >= 2) {
            val previousView = recyclerView.getChildAt(childCount - 2)?.shadowView

            previousView?.setBackgroundColor(
                ColorUtils.blendARGB(
                    ContextCompat.getColor(
                        recyclerView.context,
                        android.R.color.black
                    ), ContextCompat.getColor(
                        recyclerView.context,
                        android.R.color.transparent
                    ), abs(ratio)
                )
            )
        }
    }

    override fun hideShadow(viewHolder: RecyclerView.ViewHolder) {
        viewHolder.itemView.shadowView?.setBackgroundColor(
            ContextCompat.getColor(
                viewHolder.itemView.context,
                android.R.color.transparent
            )
        )
    }

    fun removeItem(cardItem: CardItem) {
        gameCards.remove(cardItem)
        notifyDataSetChanged()
    }

    fun getFirstVisibleElement() = gameCards.firstOrNull()

    inner class CardsViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        private val progressDrawable = getProgressDrawable(view.context)

        fun bind(cardItem: CardItem) {
            view.description.text = cardItem.characterName
            view.imageView.loadImageFromAsserts(cardItem.imageName, progressDrawable)
        }
    }
}