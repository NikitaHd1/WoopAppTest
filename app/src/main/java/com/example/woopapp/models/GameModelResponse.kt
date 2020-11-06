package com.example.woopapp.models

import com.google.gson.annotations.SerializedName

data class GameCardsResponse(
    @SerializedName("game_title")
    val title: String,
    @SerializedName("game_items")
    val gameItems: List<GameItems>
) {

    fun mapToGameCards(): GameModel {
        val cardItemList = mutableListOf<CardItem>()
        val leftVariant = gameItems.first().serial
        var rightVariant: String = leftVariant
        gameItems.forEach {
            it.items.forEach { items ->
                cardItemList.add(CardItem(items.characterName, items.imageName, it.serial))
            }
            if (it.serial.equals(rightVariant, true).not()) {
                rightVariant = it.serial
            }
        }
        return GameModel(title, cardItemList, leftVariant, rightVariant)
    }
}

data class GameItems(
    @SerializedName("serial")
    val serial: String,
    @SerializedName("items")
    val items: List<Items>
)

data class Items(
    @SerializedName("name")
    val characterName: String,
    @SerializedName("image")
    val imageName: String
)

