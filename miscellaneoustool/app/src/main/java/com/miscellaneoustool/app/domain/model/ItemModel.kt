package com.miscellaneoustool.app.domain.model

import com.miscellaneoustool.app.data.database.entity.MonsDropItem
import com.miscellaneoustool.app.ui.screen.collection.item.item.CollectionItemState
import com.miscellaneoustool.app.ui.screen.droplist.monster.item.ItemState

data class ItemModel(
    val name: String,
    val count: Int?,
    val enchantNumber: Int?
) {
    fun toItemState() = ItemState(
        name = name
    )

    fun toCollectionItemState() = CollectionItemState(
        name = name,
        count = count ?: 0,
        enchantNumber = enchantNumber ?: 0
    )

    companion object {
        fun fromMonsDropItemToDomain(monsDropItem: MonsDropItem) = ItemModel(
            name = monsDropItem.mdItemName,
            count = null,
            enchantNumber = null
        )
    }
}