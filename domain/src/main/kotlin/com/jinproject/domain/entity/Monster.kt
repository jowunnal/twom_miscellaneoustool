package com.jinproject.domain.entity

import com.jinproject.domain.entity.item.Item
import java.time.ZonedDateTime

/**
 * 몬스터 도메인 엔티티
 *
 * @param name 몬스터 이름
 * @param level 레벨
 * @param hp 체력
 * @param type 세부 타입(일반, 네임드, 보스, 대형보스)
 * @param genTime 젠 타임(죽은 뒤 재 생성 까지 걸리는 시간)
 * @param existedMap 출현 가능한 맵
 * @param dropItems 드랍 아이템
 */
data class Monster(
    val name: String,
    val level: Int,
    val hp: Int,
    val type: MonsterType,
    val genTime: Int,
    val existedMap: List<TwomMap>,
    val dropItems: List<Item>,
    val imageName: String,
) {
    fun calculateNextSpawnTime(deadTime: ZonedDateTime): ZonedDateTime =
        deadTime.plusSeconds(genTime.toLong())
}

sealed interface MonsterType {
    val name: String

    data class Normal(override val name: String) : MonsterType
    data class Named(override val name: String) : MonsterType
    data class Boss(override val name: String) : MonsterType
    data class WorldBoss(override val name: String) : MonsterType

    fun getPriority() = when (this) {
        is Normal -> 1
        is Named -> 2
        is Boss -> 3
        is WorldBoss -> 4
    }

    companion object {
        fun findByBossTypeName(bossTypeName: String) = when (bossTypeName) {
            "Normal", "일반" -> Normal(bossTypeName)
            "Mini Boss", "네임드" -> Named(bossTypeName)
            "Semi Boss", "보스" -> Boss(bossTypeName)
            "World Boss", "대형보스" -> WorldBoss(bossTypeName)
            else -> throw IllegalStateException("[$bossTypeName] 은 없는 보스 분류 입니다.")
        }
    }
}

/**
 *  맵 도메인 엔티티
 *
 *  1. 맵 이름
 *
 */
data class TwomMap(
    val name: String,
    val imageName: String,
)