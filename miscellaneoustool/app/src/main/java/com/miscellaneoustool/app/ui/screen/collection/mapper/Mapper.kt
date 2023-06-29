package com.miscellaneoustool.app.ui.screen.collection.mapper


import com.miscellaneoustool.app.ui.screen.collection.item.item.CollectionItemState
import com.miscellaneoustool.app.ui.screen.droplist.monster.item.ItemState
import com.miscellaneoustool.domain.model.ItemModel

fun ItemModel.toItemState() = ItemState(
    name = name
)

fun ItemModel.toCollectionItemState() = CollectionItemState(
    name = name,
    count = count,
    enchantNumber = enchantNumber,
    price = price
)