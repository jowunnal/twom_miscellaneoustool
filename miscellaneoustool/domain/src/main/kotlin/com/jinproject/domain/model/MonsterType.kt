package com.jinproject.domain.model

sealed interface MonsterType {
    val name: String

    data class Normal(override val name: String): MonsterType
    data class Named(override val name: String): MonsterType
    data class Boss(override val name: String): MonsterType
    data class BigBoss(override val name: String): MonsterType

    fun getPriority() = when(this) {
        is Normal -> 1
        is Named -> 2
        is Boss -> 3
        is BigBoss -> 4
    }

    companion object {
        fun findByBossTypeName(bossTypeName: String) = when(bossTypeName) {
            "Normal", "일반" -> Normal(bossTypeName)
            "Mini Boss", "네임드" -> Named(bossTypeName)
            "Semi Boss", "보스" -> Boss(bossTypeName)
            "World Boss", "대형보스" -> BigBoss(bossTypeName)
            else -> throw IllegalStateException("[$bossTypeName] 은 없는 보스 분류 입니다.")
        }
    }
}