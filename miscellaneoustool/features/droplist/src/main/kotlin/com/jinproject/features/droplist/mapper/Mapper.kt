package com.jinproject.features.droplist.mapper

import com.jinproject.domain.model.MonsterModel
import com.jinproject.features.droplist.state.MonsterState
import kotlinx.collections.immutable.toImmutableList

fun MonsterModel.toMonsterState() = MonsterState(
    name = name,
    level = level,
    genTime = genTime,
    imgName = imgName,
    type = type,
    items = item.map { itemModel ->
        itemModel.name
    }.toImmutableList()
)