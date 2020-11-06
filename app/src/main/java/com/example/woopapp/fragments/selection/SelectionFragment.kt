package com.example.woopapp.fragments.selection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.woopapp.MainViewModel
import com.example.woopapp.R
import com.example.woopapp.adapters.GameCardsAdapter
import com.example.woopapp.fragments.BaseFragment
import com.example.woopapp.adapters.SwipeableCardsAdapter
import com.example.woopapp.customview.swipe.OnSwipeListener
import com.example.woopapp.customview.swipe.SwipeLayoutManager
import com.example.woopapp.customview.swipe.SwipeTouchHelperCallback
import com.example.woopapp.models.CardItem
import com.example.woopapp.models.GameModel
import com.example.woopapp.util.ViewUtil.expand
import kotlinx.android.synthetic.main.fragment_selection.*

class SelectionFragment : BaseFragment(R.layout.fragment_selection) {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCurds().observe(viewLifecycleOwner, Observer<GameModel> { gameCards ->
            setupRecyclerView(gameCards)
            configSwipeButtons(gameCards)
            initUi(gameCards)
        })

        viewModel.navigateToResultFragmentLiveData.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(SelectionFragmentDirections.toResults())
        })
    }

    private fun initUi(gameCards: GameModel) {
        cardTitle.text = gameCards.title
        leftButton.text = gameCards.leftVariantText.replaceFirst(" ", "\n")
        rightButton.text = gameCards.rightVariantText.replaceFirst(" ", "\n")
    }

    private fun setupRecyclerView(gameModel: GameModel) {
        recyclerView.apply {
            val cards = gameModel.cardItems.toMutableList()
            cards.shuffle()
            hasFixedSize()
            adapter = GameCardsAdapter(cards)
            layoutManager = SwipeLayoutManager()
            initSwipeTouchHelper(this, cards, gameModel)
        }
    }

    private fun configSwipeButtons(gameModel: GameModel) {
        val adapter = recyclerView.adapter
        leftButton.setOnClickListener {
            if (adapter is GameCardsAdapter) {
                cardSwiped(adapter, ItemTouchHelper.LEFT, gameModel)
            }
        }
        rightButton.setOnClickListener {
            if (adapter is GameCardsAdapter) {
                cardSwiped(adapter, ItemTouchHelper.RIGHT, gameModel)
            }
        }
    }

    private fun cardSwiped(adapter: GameCardsAdapter, direction: Int, gameModel: GameModel) {
        adapter.getFirstVisibleElement()?.let { item ->
            viewModel.cardSwiped(
                item,
                getAnswerString(gameModel, direction),
                adapter.itemCount - 1
            )
            adapter.removeItem(item)
        }
    }

    private fun initSwipeTouchHelper(
        recyclerView: RecyclerView,
        cards: MutableList<CardItem>,
        gameModel: GameModel
    ) {
        val adapter = recyclerView.adapter
        if (adapter is SwipeableCardsAdapter<RecyclerView.ViewHolder>) {
            val leftInitialScaleY = leftButton.scaleY
            val leftInitialScaleX = leftButton.scaleX
            val rightInitialScaleY = rightButton.scaleY
            val rightInitialScaleX = rightButton.scaleX
            val cardCallback =
                SwipeTouchHelperCallback(
                    adapter,
                    cards,
                    object : OnSwipeListener<CardItem> {
                        override fun onSwiping(ratio: Float, direction: Int) {
                            if (direction == ItemTouchHelper.RIGHT) {
                                val calculatedRatio = -1 * ratio / 10
                                rightButton.expand(
                                    calculatedRatio,
                                    rightInitialScaleY,
                                    rightInitialScaleX
                                )
                            } else if (direction == ItemTouchHelper.LEFT) {
                                leftButton.expand(ratio / 10, leftInitialScaleY, leftInitialScaleX)
                            }
                        }

                        override fun onSwiped(element: CardItem, direction: Int) {
                            resetButtonsScale(
                                leftInitialScaleX,
                                leftInitialScaleY,
                                rightInitialScaleX,
                                rightInitialScaleY
                            )
                            viewModel.cardSwiped(
                                element,
                                getAnswerString(gameModel, direction),
                                adapter.itemCount - 1
                            )
                        }
                    })
            val touchHelper = ItemTouchHelper(cardCallback)
            touchHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun resetButtonsScale(
        leftInitialScaleX: Float,
        leftInitialScaleY: Float,
        rightInitialScaleX: Float,
        rightInitialScaleY: Float
    ) {
        leftButton.scaleX = leftInitialScaleX
        leftButton.scaleY = leftInitialScaleY
        rightButton.scaleY = rightInitialScaleY
        rightButton.scaleX = rightInitialScaleX
    }

    private fun getAnswerString(gameModel: GameModel, direction: Int) =
        if (direction == ItemTouchHelper.RIGHT) {
            gameModel.rightVariantText
        } else {
            gameModel.leftVariantText
        }
}