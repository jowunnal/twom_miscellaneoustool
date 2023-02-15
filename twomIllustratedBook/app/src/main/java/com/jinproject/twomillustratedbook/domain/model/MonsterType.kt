package com.jinproject.twomillustratedbook.domain.model

enum class MonsterType(
    val displayName: String,
    val storedName: String
) {
    NORMAL(displayName = "일반", storedName = "normal"),
    NAMED(displayName = "네임드", storedName = "named"),
    BOSS(displayName = "보스", storedName = "boss"),
    BIGBOSS(displayName = "대형보스", storedName = "bigboss");

    companion object {
        fun findByDisplayName(displayName: String) = values().first { type ->
            type.displayName == displayName
        }

        fun findByStoredName(storedName: String) =
            values().first { type -> type.storedName == storedName }
    }
}