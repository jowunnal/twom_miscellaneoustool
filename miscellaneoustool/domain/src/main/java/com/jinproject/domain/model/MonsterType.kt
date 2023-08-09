package com.jinproject.domain.model

enum class MonsterType(
    val displayName: String,
    val storedName: String
) {
    NORMAL(displayName = "일반", storedName = "normal"),
    NAMED(displayName = "네임드", storedName = "Mini Boss"),
    BOSS(displayName = "보스", storedName = "Semi Boss"),
    BIGBOSS(displayName = "대형보스", storedName = "World Boss");

    fun getPriority() = when(this) {
        NORMAL -> 1
        NAMED -> 2
        BOSS -> 3
        BIGBOSS -> 4
    }

    companion object {
        fun findByDisplayName(displayName: String) = values().find { type -> type.displayName == displayName } ?: NAMED

        fun findByStoredName(storedName: String) = values().find { type -> type.storedName == storedName } ?: NAMED
    }
}