package com.miscellaneoustool.app.data.mapper

import com.miscellaneoustool.app.data.database.entity.Book
import com.miscellaneoustool.app.data.database.entity.RegisterItemToBook
import com.miscellaneoustool.app.domain.model.CollectionModel
import com.miscellaneoustool.app.domain.model.ItemModel
import com.miscellaneoustool.app.domain.model.Stat

fun fromItemsWithStatsToCollectionModel(
    items: Map<Book, List<RegisterItemToBook>>,
    stats: Map<Book, List<com.miscellaneoustool.app.data.database.entity.Stat>>
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

private fun fromRegisterItemToDomain(registerItemToBook: RegisterItemToBook) = ItemModel(
    name = registerItemToBook.rlItemName,
    count = registerItemToBook.rlItemCount,
    enchantNumber = registerItemToBook.rlItemEnchant
)