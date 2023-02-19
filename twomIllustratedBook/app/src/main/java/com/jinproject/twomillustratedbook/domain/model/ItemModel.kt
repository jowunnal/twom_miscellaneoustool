package com.jinproject.twomillustratedbook.domain.model

import com.jinproject.twomillustratedbook.data.database.Entity.MonsDropItem
import com.jinproject.twomillustratedbook.data.database.Entity.RegisterItemToBook
import com.jinproject.twomillustratedbook.ui.screen.collection.item.item.CollectionItemState
import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.item.ItemState

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
        fun fromRegisterItemToDomain(registerItemToBook: RegisterItemToBook) = ItemModel(
            name = registerItemToBook.rlItemName,
            count = registerItemToBook.rlItemCount,
            enchantNumber = registerItemToBook.rlItemEnchant
        )

        fun fromMonsDropItemToDomain(monsDropItem: MonsDropItem) = ItemModel(
            name = monsDropItem.mdItemName,
            count = null,
            enchantNumber = null
        )
    }
}