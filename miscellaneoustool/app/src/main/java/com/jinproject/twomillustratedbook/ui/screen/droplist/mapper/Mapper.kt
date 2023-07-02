package com.jinproject.twomillustratedbook.ui.screen.droplist.mapper

import com.jinproject.twomillustratedbook.ui.screen.collection.mapper.toItemState
import com.jinproject.twomillustratedbook.ui.screen.droplist.map.item.MapState
import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.item.MonsterState
import com.jinproject.domain.model.MapModel
import com.jinproject.domain.model.MonsterModel

fun MapModel.toMapState() = MapState(
    name = name,
    imgName = imgName
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