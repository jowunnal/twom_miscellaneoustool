package com.jinproject.domain.entity.item

/**
 * 아이템 도메인 엔티티
 *
 * @param name : 아이템 이름
 * @param price : 아이템 가격
 */
abstract class Item {
    abstract val name: String
    abstract val price: Long
}

data class Miscellaneous(
    override val name: String,
    override val price: Long,
) : Item()

data class Skill(
    val limitedLevel: Int,
    override val name: String,
    override val price: Long,
) : Item()
