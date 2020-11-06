package com.example.woopapp.customview.swipe

interface OnSwipeListener<T> {

    fun onSwiping(ratio: Float, direction: Int)

    fun onSwiped(element: T, direction: Int)
}