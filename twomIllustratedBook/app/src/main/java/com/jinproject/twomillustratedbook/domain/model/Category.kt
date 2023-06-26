package com.jinproject.twomillustratedbook.domain.model

enum class Category(val displayName: String,val storedName: String) {
    MISCELLANEOUS(displayName = "잡탬류", storedName = "miscellaneous"),
    WEAPONS(displayName = "무기류", storedName = "weapon"),
    ARMORS(displayName = "방어구류", storedName = "armor"),
    COSTUMES(displayName = "코스튬류", storedName = "costume");
    // SKILLS(displayName = "스킬류", storedName = "skill")

    companion object {
        fun findByStoredName(storedName: String) =
            when(storedName) {
                "miscellaneous" -> MISCELLANEOUS
                "weapon" -> WEAPONS
                "armor" -> ARMORS
                "costume" -> COSTUMES
                else -> throw IllegalStateException("잘못된 카테고리 입니다.")
            }
    }
}