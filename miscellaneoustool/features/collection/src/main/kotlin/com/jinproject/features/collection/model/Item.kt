package com.jinproject.features.collection.model

import com.jinproject.domain.model.ItemModel
import com.jinproject.domain.model.ItemType

abstract class Item {
    abstract val name: String
    abstract val count: Int
    abstract val price: Long

    companion object {
        fun Item.toItemModel() = ItemModel(
            name = name,
            count = count,
            enchantNumber = 0,
            price = price,
            type = ItemType.Miscellaneous(name)
        )
    }
}

data class Equipment(
    override val name: String,
    override val count: Int,
    val enchantNumber: Int,
    override val price: Long,
): Item()

data class MiscellaneousItem(
    override val name: String,
    override val count: Int,
    override val price: Long,
): Item()