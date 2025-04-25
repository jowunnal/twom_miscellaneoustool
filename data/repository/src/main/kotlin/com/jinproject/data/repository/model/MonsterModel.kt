package com.jinproject.data.repository.model

import com.jinproject.domain.entity.Monster
import com.jinproject.domain.entity.MonsterType

data class MonsterModel(
    val name: String,
    val level: Int,
    val genTime: Int,
    val imgName: String,
    val type: String,
    val item: List<GetItemDomainFactory>
) {
    fun toMonsterDomain() = Monster(
        name = name,
        level = level,
        hp = 0,
        type = MonsterType.findByBossTypeName(type),
        genTime = genTime,
        existedMap = emptyList(),
        dropItems = item.map { it.getDomainFactory().create() },
        imageName = imgName,
    )
}

fun List<MonsterModel>.toDomainModels() = mapNotNull {
    runCatching { it.toMonsterDomain() }.getOrNull()
}