package com.jinproject.features.collection.mapper


import com.jinproject.domain.model.ItemModel
import com.jinproject.features.collection.item.item.CollectionItemState

fun ItemModel.toCollectionItemState() = CollectionItemState(
    name = name,
    count = count,
    enchantNumber = enchantNumber,
    price = price
)