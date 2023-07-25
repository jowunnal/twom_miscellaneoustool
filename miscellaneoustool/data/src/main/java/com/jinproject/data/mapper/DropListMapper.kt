package com.jinproject.data.mapper

import com.jinproject.data.datasource.cache.database.entity.MonsDropItem
import com.jinproject.data.datasource.cache.database.entity.Monster
import com.jinproject.domain.model.ItemModel
import com.jinproject.domain.model.MapField
import com.jinproject.domain.model.MapModel
import com.jinproject.domain.model.MonsterModel
import com.jinproject.domain.model.MonsterType

fun MonsDropItem.toItemModel() = ItemModel(
    name = mdItemName,
    count = 0,
    enchantNumber = 0,
    price = 0
)

fun com.jinproject.data.datasource.cache.database.entity.Maps.toMapModel() = MapModel(
    name = MapField.findByMapName(mapName),
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

fun fromItemsToItemModel(items: List<com.jinproject.data.datasource.cache.database.entity.Item>) =
    items.map { item ->
        ItemModel(
            name = item.itemName,
            count = 0,
            enchantNumber = 0,
            price = item.itemPrice
        )
    }