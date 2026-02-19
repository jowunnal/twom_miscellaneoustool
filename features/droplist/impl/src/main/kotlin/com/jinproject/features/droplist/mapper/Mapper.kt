package com.jinproject.features.droplist.mapper

import com.jinproject.domain.entity.Monster
import com.jinproject.features.droplist.state.MonsterState
import com.jinproject.features.droplist.state.toItemStateList
import kotlinx.collections.immutable.toImmutableList

fun Monster.toMonsterState() = MonsterState(
    name = name,
    level = level,
    genTime = genTime,
    imageName = imageName,
    type = type,
    items = dropItems.toItemStateList().toImmutableList()
)