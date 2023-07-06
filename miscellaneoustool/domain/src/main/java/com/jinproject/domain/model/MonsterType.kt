package com.jinproject.domain.model

enum class MonsterType(
    val displayName: String,
    val storedName: String
) {
    NORMAL(displayName = "일반", storedName = "normal"),
    NAMED(displayName = "네임드", storedName = "named"),
    BOSS(displayName = "보스", storedName = "boss"),
    BIGBOSS(displayName = "대형보스", storedName = "bigboss");

    companion object {
        fun findByDisplayName(displayName: String) = values().find { type -> type.displayName == displayName } ?: NAMED

        fun findByStoredName(storedName: String) = values().find { type -> type.storedName == storedName } ?: NAMED
    }
}