package com.jinproject.domain.entity.item

/**
 * 아이템 도메인 엔티티
 *
 * @param name : 아이템 이름
 * @param price : 아이템 가격
 * @param imageName : 아이템 이미지 이름
 */
abstract class Item {
    abstract val name: String
    abstract val price: Long
    abstract val imageName: String
}

data class Miscellaneous(
    override val name: String,
    override val price: Long,
    override val imageName: String,
) : Item()

data class Skill(
    val limitedLevel: Int,
    override val name: String,
    override val price: Long,
    override val imageName: String,
) : Item()
