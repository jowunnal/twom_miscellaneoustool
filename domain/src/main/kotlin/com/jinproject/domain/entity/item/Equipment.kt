package com.jinproject.domain.entity.item

/**
 * 아이모에 존재하는 장비류 아이템 도메인 엔티티
 *
 * @param stats : 장비 아이템의 스텟
 * @param limitedLevel : 장비 아이템의 레벨 제한
 */
abstract class Equipment : Item() {
    abstract val stats: Map<String, Float>
    abstract val limitedLevel: Int
    abstract val type: ItemType
}

abstract class EnchantableEquipment : Equipment(), Enchant {
    abstract val uuid: String
}

data class Weapon(
    override val stats: Map<String, Float>,
    override val limitedLevel: Int,
    override val name: String,
    override val price: Long,
    override val type: ItemType,
    override val uuid: String,
    override val imageName: String,
    val speed: Int,
    val damageRange: IntRange,
) : EnchantableEquipment() {

    override var enchantNumber: Int = 0
    override fun isAvailableScroll(scroll: Scroll): Boolean {
        return if (scroll is WeaponScroll)
            limitedLevel in scroll.grade.range
        else
            false
    }

    override val enchantProbability: Int
        get() = when (enchantNumber) {
            in 0..5 -> 100
            6 -> 30
            7 -> 20
            8 -> 10
            else -> 5
        }

    override fun enchant(): Equipment? {
        return if (process()) this.apply { increaseEnchantNumber() } else null
    }
}

data class Armor(
    override val stats: Map<String, Float>,
    override val limitedLevel: Int,
    override val name: String,
    override val price: Long,
    override val type: ItemType,
    override val uuid: String,
    override val imageName: String,
    val armor: Int,
) : EnchantableEquipment() {

    override var enchantNumber: Int = 0
    override fun isAvailableScroll(scroll: Scroll): Boolean {
        return if (scroll is ArmorScroll)
            limitedLevel in scroll.grade.range
        else
            false
    }

    override val enchantProbability: Int
        get() = when (enchantNumber) {
            in 0..3 -> 100
            4 -> 30
            5 -> 20
            6 -> 10
            else -> 5
        }

    override fun enchant(): Equipment? {
        return if (process()) this.apply { increaseEnchantNumber() } else null
    }
}

data class Accessory(
    override val stats: Map<String, Float>,
    override val limitedLevel: Int,
    override val name: String,
    override val price: Long,
    override val type: ItemType,
    override val uuid: String,
    override val imageName: String,
    val armor: Int,
) : EnchantableEquipment() {

    private var enchantScrollType: StatEnchantScrollType? = null

    override var enchantNumber: Int = 0

    override fun isAvailableScroll(scroll: Scroll): Boolean {
        return if (scroll is StatScroll) {
            enchantScrollType = scroll.stat
            true
        } else
            false
    }

    override val enchantProbability: Int
        get() = when (enchantNumber) {
            0 -> 20
            1 -> 5
            else -> 1
        }

    /**
     * 강화 실행
     *
     * @exception IllegalArgumentException 강화 주문서가 등록되지 않은 경우
     */
    override fun enchant(): Equipment? {
        if (enchantScrollType == null)
            throw IllegalArgumentException("강화 주문서가 등록되지 않았습니다.")

        return if (process()) this.apply { increaseEnchantNumber() } else null
    }
}

data class Costume(
    override val stats: Map<String, Float>,
    override val limitedLevel: Int,
    override val name: String,
    override val price: Long,
    override val type: ItemType,
    override val imageName: String,
) : Equipment()
