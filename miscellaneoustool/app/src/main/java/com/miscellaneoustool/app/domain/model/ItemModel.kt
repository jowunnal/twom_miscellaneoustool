package com.miscellaneoustool.app.domain.model

import com.miscellaneoustool.app.data.datasource.cache.database.entity.MonsDropItem
import com.miscellaneoustool.app.ui.screen.collection.item.item.CollectionItemState
import com.miscellaneoustool.app.ui.screen.droplist.monster.item.ItemState

data class ItemModel(
    val name: String,
    val count: Int,
    val enchantNumber: Int,
    val price: Int
) {
    fun toItemState() = ItemState(
        name = name
    )

    fun toCollectionItemState() = CollectionItemState(
        name = name,
        count = count,
        enchantNumber = enchantNumber,
        price = price
    )

    companion object {
        fun fromMonsDropItemToDomain(monsDropItem: MonsDropItem) = ItemModel(
            name = monsDropItem.mdItemName,
            count = 0,
            enchantNumber = 0,
            price = 0
        )
    }
}