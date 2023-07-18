package com.jinproject.features.droplist.mapper

import com.jinproject.domain.model.ItemModel
import com.jinproject.domain.model.MapModel
import com.jinproject.domain.model.MonsterModel
import com.jinproject.features.droplist.map.item.MapState
import com.jinproject.features.droplist.monster.item.ItemState
import com.jinproject.features.droplist.monster.item.MonsterState

fun MapModel.toMapState() = MapState(
    name = name,
    imgName = imgName
)

fun ItemModel.toItemState() = ItemState(
    name = name
)

fun MonsterModel.toMonsterState() = MonsterState(
    name = name,
    level = level,
    genTime = genTime,
    imgName = imgName,
    type = type,
    item = item.map { itemModel ->
        itemModel.toItemState()
    }
)