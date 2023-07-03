package com.jinproject.data.mapper

import com.jinproject.data.datasource.cache.database.entity.Book
import com.jinproject.data.datasource.cache.database.entity.MonsDropItem
import com.jinproject.data.datasource.cache.database.entity.Monster
import com.jinproject.data.model.Item
import com.jinproject.domain.model.CollectionModel
import com.jinproject.domain.model.ItemModel
import com.jinproject.domain.model.MapModel
import com.jinproject.domain.model.MonsterModel
import com.jinproject.domain.model.MonsterType
import com.jinproject.domain.model.Stat
import com.jinproject.domain.model.TimerModel
import com.jinproject.domain.model.WeekModel

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

fun fromItemsToItemModel(items: List<com.jinproject.data.datasource.cache.database.entity.Item>) =
    items.map { item ->
        ItemModel(
            name = item.itemName,
            count = 0,
            enchantNumber = 0,
            price = item.itemPrice
        )
    }

fun MonsDropItem.toItemModel() = ItemModel(
    name = mdItemName,
    count = 0,
    enchantNumber = 0,
    price = 0
)

fun com.jinproject.data.datasource.cache.database.entity.Maps.toMapModel() = MapModel(
    name = mapName,
    imgName = mapImgName
)

fun Monster.toMonsterModel() = MonsterModel(
    name = monsName,
    level = monsLevel,
    genTime = monsGtime,
    imgName = monsImgName,
    type = MonsterType.findByStoredName(monsType),
    item = emptyList()
)

fun com.jinproject.data.datasource.cache.database.entity.Timer.toTimerModel() = TimerModel(
    id = timerId,
    bossName = timerMonsName,
    day = WeekModel.findByCode(day),
    hour = hour,
    minutes = min,
    seconds = sec,
    isOverlayOnOrNot = ota == 1
)