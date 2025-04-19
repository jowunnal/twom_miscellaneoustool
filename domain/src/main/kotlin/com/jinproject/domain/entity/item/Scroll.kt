package com.jinproject.domain.entity.item

/**
 * 주문서 도메인 엔티티
 */
abstract class Scroll : Item()

abstract class GradeScroll : Scroll() {
    abstract val grade: Grade

    enum class Grade(val range: IntRange) {
        S(40..49), A(30..39), B(20..29), C(10..19), D(0..9)
    }
}

data class WeaponScroll(
    override val name: String = "무기 주문서",
    override val price: Long = 0,
    override val grade: Grade,
    override val imageName: String,
) : GradeScroll()

data class ArmorScroll(
    override val name: String = "방어구 주문서",
    override val price: Long = 0,
    override val grade: Grade,
    override val imageName: String,
) : GradeScroll()

enum class StatEnchantScrollType {
    STR,
    DEX,
    INT,
    HP,
    MP
}

data class StatScroll(
    val stat: StatEnchantScrollType,
    override val name: String = "${stat.name} 주문서",
    override val price: Long = 0,
    override val imageName: String,
) : Scroll()