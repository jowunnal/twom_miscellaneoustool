package com.jinproject.twomillustratedbook.ui.screen.collection.mapper


import com.jinproject.twomillustratedbook.ui.screen.collection.item.item.CollectionItemState
import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.item.ItemState
import com.jinproject.domain.model.ItemModel

fun ItemModel.toItemState() = ItemState(
    name = name
)

fun ItemModel.toCollectionItemState() = CollectionItemState(
    name = name,
    count = count,
    enchantNumber = enchantNumber,
    price = price
)