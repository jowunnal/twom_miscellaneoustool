package com.jinproject.features.collection.model

import android.util.Log
import com.jinproject.domain.entity.item.EnchantableEquipment
import com.jinproject.domain.model.ItemModel
import com.jinproject.domain.model.ItemType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

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

        fun fromDomainItem(items: List<com.jinproject.domain.entity.item.Item>): ImmutableList<Item> =
            if (items.isNotEmpty())
                items.filter {
                    it !is EnchantableEquipment
                }.groupBy { it::class }.entries.map { itemEntry ->
                    val items = itemEntry.value

                    val miscellaneousItems = items.first()

                    MiscellaneousItem(
                        name = miscellaneousItems.name,
                        count = items.size,
                        price = miscellaneousItems.price,
                    )
                }.plus(
                    items.filterIsInstance<EnchantableEquipment>().map { item ->
                        Equipment(
                            name = item.name,
                            count = 0,
                            enchantNumber = item.enchantNumber,
                            price = item.price,
                        )
                    }
                ).toImmutableList()
            else
                persistentListOf()
    }
}

data class Equipment(
    override val name: String,
    override val count: Int,
    val enchantNumber: Int,
    override val price: Long,
) : Item()

data class MiscellaneousItem(
    override val name: String,
    override val count: Int,
    override val price: Long,
) : Item()