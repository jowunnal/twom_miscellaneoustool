package com.jinproject.data.repository.model

import com.jinproject.domain.model.ItemType

data class ItemModel(
    val name: String,
    val count: Int,
    val enchantNumber: Int,
    val price: Long,
    val type: String,
)

fun ItemModel.toDomainModel() = com.jinproject.domain.model.ItemModel(
    name = name,
    count = count,
    enchantNumber = enchantNumber,
    price = price,
    type = ItemType.findByStoredName(type),
)

fun List<ItemModel>.toDomainModels() = mapNotNull { item ->
    runCatching {
        item.toDomainModel()
    }.getOrNull()
}
