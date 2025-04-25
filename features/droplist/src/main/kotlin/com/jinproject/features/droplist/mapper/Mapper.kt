package com.jinproject.features.droplist.mapper

import com.jinproject.domain.entity.Monster
import com.jinproject.features.droplist.state.MonsterState
import kotlinx.collections.immutable.toImmutableList

fun Monster.toMonsterState() = MonsterState(
    name = name,
    level = level,
    genTime = genTime,
    imgName = imageName,
    type = type,
    items = dropItems.map { itemModel ->
        itemModel.name
    }.toImmutableList()
)