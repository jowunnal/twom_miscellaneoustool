package com.jinproject.domain.model

sealed interface ItemType {
    val name: String

    data class Miscellaneous(override val name: String): ItemType
    data class Weapon(override val name: String): ItemType
    data class Armor(override val name: String): ItemType
    data class Accessory(override val name: String): ItemType
    data class Costume(override val name: String): ItemType
    data class Skill(override val name: String): ItemType

    companion object {
        fun findByStoredName(storedName: String): ItemType =
            when(storedName) {
                "잡탬", "miscellaneous" -> Miscellaneous(storedName)
                "무기", "weapon" -> Weapon(storedName)
                "방어구", "armor" -> Armor(storedName)
                "코스튬", "costume" -> Costume(storedName)
                "스킬", "skill" -> Skill(storedName)
                "장신구", "accessory" -> Accessory(storedName)
                else -> throw IllegalStateException("[$storedName] 은 잘못된 카테고리 입니다.")
            }
    }
}

fun Iterable<String>.getItemCategory(): ItemType =
    if (this.find { it == "MINDAMAGE" } != null) ItemType.Weapon("") else ItemType.Armor("")