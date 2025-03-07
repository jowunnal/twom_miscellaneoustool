package com.jinproject.data.repository.model

import com.jinproject.domain.model.MonsterType

data class MonsterModel(
    val name: String,
    val level: Int,
    val genTime: Int,
    val imgName: String,
    val type: String,
    val item: List<ItemModel>
)

fun MonsterModel.toDomainModel() = com.jinproject.domain.model.MonsterModel(
    name = name,
    level = level,
    genTime = genTime,
    imgName = imgName,
    type = MonsterType.findByBossTypeName(type),
    item = item.toDomainModels()
)

fun List<MonsterModel>.toDomainModels() = mapNotNull {
    runCatching { it.toDomainModel() }.getOrNull()
}