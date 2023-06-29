package com.miscellaneoustool.data.mapper

import com.miscellaneoustool.data.datasource.cache.database.entity.Book
import com.miscellaneoustool.data.datasource.cache.database.entity.MonsDropItem
import com.miscellaneoustool.data.datasource.cache.database.entity.Monster
import com.miscellaneoustool.data.model.Item
import com.miscellaneoustool.domain.model.CollectionModel
import com.miscellaneoustool.domain.model.ItemModel
import com.miscellaneoustool.domain.model.MapModel
import com.miscellaneoustool.domain.model.MonsterModel
import com.miscellaneoustool.domain.model.MonsterType
import com.miscellaneoustool.domain.model.Stat
import com.miscellaneoustool.domain.model.TimerModel
import com.miscellaneoustool.domain.model.WeekModel

fun fromItemsWithStatsToCollectionModel(
    items: Map<Book, List<Item>>,
    stats: Map<Book, List<com.miscellaneoustool.data.datasource.cache.database.entity.Stat>>
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

fun fromItemsToItemModel(items: List<com.miscellaneoustool.data.datasource.cache.database.entity.Item>) =
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

fun com.miscellaneoustool.data.datasource.cache.database.entity.Maps.toMapModel() = MapModel(
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

fun com.miscellaneoustool.data.datasource.cache.database.entity.Timer.toTimerModel() = TimerModel(
    id = timerId,
    bossName = timerMonsName,
    day = WeekModel.findByCode(day),
    hour = hour,
    minutes = min,
    seconds = sec,
    isOverlayOnOrNot = ota == 1
)