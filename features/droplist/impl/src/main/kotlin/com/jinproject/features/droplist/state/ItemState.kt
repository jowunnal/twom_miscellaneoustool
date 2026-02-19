package com.jinproject.features.droplist.state

import com.jinproject.domain.entity.item.Item

data class ItemState(
    override val name: String,
    override val imageName: String
) : Searchable {
    override val imagePrefix: String = "item"
}

fun Item.toItemState(): ItemState = ItemState(
    name = name,
    imageName = imageName
)

fun List<Item>.toItemStateList(): List<ItemState> = map { it.toItemState() }
