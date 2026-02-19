package com.jinproject.features.home.model

import com.jinproject.domain.entity.item.EnchantableEquipment
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

abstract class Item {
    abstract val name: String
    abstract val count: Int
    abstract val price: Long
    abstract val imageName: String

    companion object {
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
                        imageName = miscellaneousItems.imageName,
                    )
                }.plus(
                    items.filterIsInstance<EnchantableEquipment>().map { item ->
                        Equipment(
                            name = item.name,
                            count = 0,
                            enchantNumber = item.enchantNumber,
                            price = item.price,
                            imageName = item.imageName
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
    override val imageName: String,
) : Item()

data class MiscellaneousItem(
    override val name: String,
    override val count: Int,
    override val price: Long,
    override val imageName: String,
) : Item()
