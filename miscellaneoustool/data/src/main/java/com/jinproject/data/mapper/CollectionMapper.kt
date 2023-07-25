package com.jinproject.data.mapper

import com.jinproject.data.datasource.cache.database.entity.Book
import com.jinproject.data.model.Item
import com.jinproject.domain.model.CollectionModel
import com.jinproject.domain.model.ItemModel
import com.jinproject.domain.model.Stat

fun fromItemsWithStatsToCollectionModel(
    items: Map<Book, List<Item>>,
    stats: Map<Book, List<com.jinproject.data.datasource.cache.database.entity.Stat>>
) =
    items.map { eachItem ->
        CollectionModel(
            bookId = eachItem.key.bookId,
            stat = mutableMapOf<Stat, Double>().apply {
                runCatching {
                    stats[eachItem.key]!!.forEach { stat ->
                        put(Stat.valueOf(stat.type), stat.value)
                    }
                }.getOrDefault(emptyMap<Stat, Double>())
            },
            items = eachItem.value.map { item -> fromRegisterItemToDomain(item) }
        )
    }

private fun fromRegisterItemToDomain(item: Item) = ItemModel(
    name = item.name,
    count = item.count,
    enchantNumber = item.enchant,
    price = item.price
)
