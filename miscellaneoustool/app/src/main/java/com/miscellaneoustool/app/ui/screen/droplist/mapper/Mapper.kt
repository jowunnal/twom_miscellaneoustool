package com.miscellaneoustool.app.ui.screen.droplist.mapper

import com.miscellaneoustool.app.ui.screen.collection.mapper.toItemState
import com.miscellaneoustool.app.ui.screen.droplist.map.item.MapState
import com.miscellaneoustool.app.ui.screen.droplist.monster.item.MonsterState
import com.miscellaneoustool.domain.model.MapModel
import com.miscellaneoustool.domain.model.MonsterModel

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