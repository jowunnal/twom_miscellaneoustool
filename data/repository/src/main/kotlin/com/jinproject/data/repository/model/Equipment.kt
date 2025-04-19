package com.jinproject.data.repository.model

import com.jinproject.data.repository.model.EquipmentDomainFactory.Companion.ARMOR_ELSE
import com.jinproject.data.repository.model.EquipmentDomainFactory.Companion.ARMOR_KO
import com.jinproject.data.repository.model.EquipmentDomainFactory.Companion.MAX_DAMAGE_ELSE
import com.jinproject.data.repository.model.EquipmentDomainFactory.Companion.MAX_DAMAGE_KO
import com.jinproject.data.repository.model.EquipmentDomainFactory.Companion.MIN_DAMAGE_ELSE
import com.jinproject.data.repository.model.EquipmentDomainFactory.Companion.MIN_DAMAGE_KO
import com.jinproject.data.repository.model.EquipmentDomainFactory.Companion.SPEED_ELSE
import com.jinproject.data.repository.model.EquipmentDomainFactory.Companion.SPEED_KO
import com.jinproject.domain.entity.item.Accessory
import com.jinproject.domain.entity.item.Armor
import com.jinproject.domain.entity.item.Costume
import com.jinproject.domain.entity.item.EnchantableEquipment
import com.jinproject.domain.entity.item.ItemType
import com.jinproject.domain.entity.item.Weapon
import java.util.Locale

data class Equipment(
    val info: EquipmentInfo,
    val data: EquipmentEntity,
) {
    fun toDomain(): com.jinproject.domain.entity.item.Equipment {
        val factory = data.itemType.itemTypeToEquipmentDomainFactory()

        return (factory.create(
            equipment = this,
            stats = info.stats.toMutableMap(),
        ) as EnchantableEquipment).apply {
            this@apply.enchantNumber = this@Equipment.info.enchantNumber
        }
    }

    companion object {
        fun String.itemTypeToEquipmentDomainFactory(): EquipmentDomainFactory =
            when (this) {
                "무기", "weapon" -> WeaponDomainFactory
                "방어구", "armor" -> ArmorDomainFactory
                "장신구", "accessories" -> AccessoryDomainFactory
                "코스튬", "costume" -> ArmorDomainFactory
                else -> throw IllegalArgumentException("[$this] 는 장비 아이템 타입이 아닙니다.")
            }

        fun toItemType(equipment: EnchantableEquipment) = when (equipment) {
            is Weapon -> "무기"
            is Armor -> "방어구"
            is Accessory -> "장신구"
            else -> throw IllegalArgumentException("[$this] 는 강화 가능한 장비 아이템 타입이 아닙니다.")
        }
    }
}

data class EquipmentInfo(
    val name: String,
    val uuid: String,
    val enchantNumber: Int,
    val stats: Map<String, Float>,
) {
    fun toEquipment(equipmentEntity: EquipmentEntity? = null) = Equipment(
        info = this,
        data = equipmentEntity ?: EquipmentEntity.getInitValues()
    )

    companion object {
        fun getInitValues() = EquipmentInfo(
            name = "버닝블레이드",
            uuid = "",
            enchantNumber = 0,
            stats = emptyMap(),
        )
    }
}

data class EquipmentEntity(
    val level: Int,
    val imageName: String,
    val itemType: String,
) {
    fun toEquipment(equipmentInfo: EquipmentInfo? = null) = Equipment(
        info = equipmentInfo ?: EquipmentInfo.getInitValues(),
        data = this,
    )

    companion object {
        fun getInitValues() = EquipmentEntity(
            level = 0,
            imageName = "burning_blade",
            itemType = "무기",
        )
    }
}

abstract class EquipmentDomainFactory {
    abstract fun create(
        equipment: Equipment,
        language: String = Locale.getDefault().language,
        stats: MutableMap<String, Float>,
    ): com.jinproject.domain.entity.item.Equipment

    companion object {
        fun EnchantableEquipment.getEquipmentFactory(): EquipmentDomainFactory = when (this) {
            is Weapon -> WeaponDomainFactory
            is Armor -> ArmorDomainFactory
            is Accessory -> AccessoryDomainFactory
            else -> throw IllegalArgumentException("Unknown equipment type")
        }

        const val SPEED_KO = "속도"
        const val SPEED_ELSE = "Speed"
        const val MIN_DAMAGE_KO = "최소데미지"
        const val MIN_DAMAGE_ELSE = "MinDamage"
        const val MAX_DAMAGE_KO = "최대데미지"
        const val MAX_DAMAGE_ELSE = "MaxDamage"
        const val ARMOR_KO = "방어"
        const val ARMOR_ELSE = "Armor"
    }
}

object WeaponDomainFactory : EquipmentDomainFactory() {
    override fun create(
        equipment: Equipment,
        language: String,
        stats: MutableMap<String, Float>
    ): com.jinproject.domain.entity.item.Equipment {
        val speed = stats.remove(
            language.onLanguage(
                ko = SPEED_KO,
                en = SPEED_ELSE,
            )
        )?.toInt() ?: 0
        val minDamage = stats.remove(
            language.onLanguage(
                ko = MIN_DAMAGE_KO,
                en = MIN_DAMAGE_ELSE,
            )
        )?.toInt() ?: 0
        val maxDamage = stats.remove(
            language.onLanguage(
                ko = MAX_DAMAGE_KO,
                en = MAX_DAMAGE_ELSE,
            )
        )?.toInt() ?: 0

        return Weapon(
            stats = stats,
            limitedLevel = equipment.data.level,
            name = equipment.info.name,
            price = 0,
            type = ItemType.NORMAL,
            speed = speed,
            damageRange = minDamage..maxDamage,
            uuid = equipment.info.uuid,
            imageName = equipment.data.imageName,
        )
    }
}

object ArmorDomainFactory : EquipmentDomainFactory() {
    override fun create(
        equipment: Equipment,
        language: String,
        stats: MutableMap<String, Float>
    ): com.jinproject.domain.entity.item.Equipment {
        val armor = stats.remove(
            language.onLanguage(
                ko = MAX_DAMAGE_KO,
                en = MAX_DAMAGE_ELSE,
            )
        )?.toInt() ?: 0

        return Armor(
            stats = stats,
            limitedLevel = equipment.data.level,
            name = equipment.info.name,
            price = 0,
            type = ItemType.NORMAL,
            armor = armor,
            uuid = equipment.info.uuid,
            imageName = equipment.data.imageName,
        )
    }
}

object AccessoryDomainFactory : EquipmentDomainFactory() {
    override fun create(
        equipment: Equipment,
        language: String,
        stats: MutableMap<String, Float>
    ): com.jinproject.domain.entity.item.Equipment {
        val armor = stats.remove(
            language.onLanguage(
                ko = MAX_DAMAGE_KO,
                en = MAX_DAMAGE_ELSE,
            )
        )?.toInt() ?: 0

        return Accessory(
            stats = equipment.info.stats,
            limitedLevel = equipment.data.level,
            name = equipment.info.name,
            price = 0,
            type = ItemType.NORMAL,
            armor = armor,
            uuid = equipment.info.uuid,
            imageName = equipment.data.imageName,
        )
    }
}

object CostumeDomainFactory : EquipmentDomainFactory() {
    override fun create(
        equipment: Equipment,
        language: String,
        stats: MutableMap<String, Float>
    ): com.jinproject.domain.entity.item.Equipment {
        return Costume(
            stats = equipment.info.stats,
            limitedLevel = equipment.data.level,
            name = equipment.info.name,
            price = 0,
            type = ItemType.NORMAL,
            imageName = equipment.data.imageName,
        )
    }
}

private fun String.onLanguage(ko: String, en: String) = if (this == "ko")
    ko
else
    en

fun List<EquipmentEntity>.toDomain(): List<com.jinproject.domain.entity.item.Equipment> =
    map { it.toEquipment().toDomain() }

fun EnchantableEquipment.toEquipmentData() = Equipment(
    info = EquipmentInfo(
        name = name,
        enchantNumber = enchantNumber,
        uuid = uuid,
        stats = stats.toMutableMap().apply {
            val language = Locale.getDefault().language

            when (this@toEquipmentData) {
                is Weapon -> {
                    put(
                        language.onLanguage(
                            ko = SPEED_KO,
                            en = SPEED_ELSE,
                        ), speed.toFloat()
                    )
                    put(
                        language.onLanguage(
                            ko = MIN_DAMAGE_KO,
                            en = MIN_DAMAGE_ELSE,
                        ), damageRange.first.toFloat()
                    )
                    put(
                        language.onLanguage(
                            ko = MAX_DAMAGE_KO,
                            en = MAX_DAMAGE_ELSE
                        ), damageRange.last.toFloat()
                    )
                }

                is Armor -> {
                    put(
                        language.onLanguage(
                            ko = ARMOR_KO,
                            en = ARMOR_ELSE,
                        ), armor.toFloat()
                    )
                }

                is Accessory -> {
                    put(
                        language.onLanguage(
                            ko = ARMOR_KO,
                            en = ARMOR_ELSE,
                        ), armor.toFloat()
                    )
                }
            }
        },
    ),
    data = EquipmentEntity(
        level = limitedLevel,
        itemType = Equipment.toItemType(this),
        imageName = imageName,
    ),
)
