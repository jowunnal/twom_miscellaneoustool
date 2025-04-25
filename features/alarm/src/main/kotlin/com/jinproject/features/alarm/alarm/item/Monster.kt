package com.jinproject.features.alarm.alarm.item

import com.jinproject.domain.entity.Monster

data class MonsterState(
    val name: String,
    val type: MonsterType,
    val imageName: String,
) : Comparable<MonsterState> {
    override fun compareTo(other: MonsterState): Int {
        return if (this.type.getPriority() == other.type.getPriority())
            this.name.compareTo(other.name)
        else
            this.type.getPriority().compareTo(other.type.getPriority())
    }

    companion object {
        fun fromMonsterDomain(domain: Monster) = MonsterState(
            name = domain.name,
            type = MonsterType.fromDomain(domain.type),
            imageName = domain.imageName,
        )
    }
}

enum class MonsterType {
    Normal,
    Named,
    Boss,
    WorldBoss;

    fun getPriority() = when (this) {
        Normal -> 0
        Named -> 1
        Boss -> 2
        WorldBoss -> 3
    }

    companion object {
        fun fromDomain(domain: com.jinproject.domain.entity.MonsterType): MonsterType = when (domain) {
            is com.jinproject.domain.entity.MonsterType.Normal -> Normal
            is com.jinproject.domain.entity.MonsterType.Named -> Named
            is com.jinproject.domain.entity.MonsterType.Boss -> Boss
            is com.jinproject.domain.entity.MonsterType.WorldBoss -> WorldBoss
        }
    }
}