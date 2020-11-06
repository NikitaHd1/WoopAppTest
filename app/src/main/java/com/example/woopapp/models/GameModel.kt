package com.example.woopapp.models

data class GameModel(
    val title: String,
    val cardItems: List<CardItem>,
    val leftVariantText: String,
    val rightVariantText: String
)

data class CardItem(
    val characterName: String,
    val imageName: String,
    val serial: String
)