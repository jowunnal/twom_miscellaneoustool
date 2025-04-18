package com.jinproject.data.repository.model

import com.jinproject.data.repository.model.EquipmentFactory.Companion.ARMOR_ELSE
import com.jinproject.data.repository.model.EquipmentFactory.Companion.ARMOR_KO
import com.jinproject.data.repository.model.EquipmentFactory.Companion.MAX_DAMAGE_ELSE
import com.jinproject.data.repository.model.EquipmentFactory.Companion.MAX_DAMAGE_KO
import com.jinproject.data.repository.model.EquipmentFactory.Companion.MIN_DAMAGE_ELSE
import com.jinproject.data.repository.model.EquipmentFactory.Companion.MIN_DAMAGE_KO
import com.jinproject.data.repository.model.EquipmentFactory.Companion.SPEED_ELSE
import com.jinproject.data.repository.model.EquipmentFactory.Companion.SPEED_KO
import com.jinproject.data.repository.model.EquipmentFactory.Companion.getEquipmentFactory
import com.jinproject.domain.entity.item.Accessory
import com.jinproject.domain.entity.item.Armor
import com.jinproject.domain.entity.item.Costume
import com.jinproject.domain.entity.item.EnchantableEquipment
import com.jinproject.domain.entity.item.ItemType
import com.jinproject.domain.entity.item.Weapon
import java.util.Locale

data class Equipment(
    val name: String,
    val level: Int,
    val stats: Map<String, Float>,
    val enchantNumber: Int,
    val factory: EquipmentFactory,
    val uuid: String,
)

abstract class EquipmentFactory {
    abstract fun create(
        equipment: Equipment,
        language: String = Locale.getDefault().language,
        stats: MutableMap<String, Float>,
    ): com.jinproject.domain.entity.item.Equipment

    companion object {
        fun EnchantableEquipment.getEquipmentFactory(): EquipmentFactory = when (this) {
            is Weapon -> WeaponFactory
            is Armor -> ArmorFactory
            is Accessory -> AccessoryFactory
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

object WeaponFactory : EquipmentFactory() {
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
            limitedLevel = equipment.level,
            name = equipment.name,
            price = 0,
            type = ItemType.NORMAL,
            speed = speed,
            damageRange = minDamage..maxDamage,
            uuid = equipment.uuid,
        )
    }
}

object ArmorFactory : EquipmentFactory() {
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
            limitedLevel = equipment.level,
            name = equipment.name,
            price = 0,
            type = ItemType.NORMAL,
            armor = armor,
            uuid = equipment.uuid,
        )
    }
}

object AccessoryFactory : EquipmentFactory() {
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
            stats = equipment.stats,
            limitedLevel = equipment.level,
            name = equipment.name,
            price = 0,
            type = ItemType.NORMAL,
            armor = armor,
            uuid = equipment.uuid,
        )
    }
}

object CostumeFactory : EquipmentFactory() {
    override fun create(
        equipment: Equipment,
        language: String,
        stats: MutableMap<String, Float>
    ): com.jinproject.domain.entity.item.Equipment {
        return Costume(
            stats = equipment.stats,
            limitedLevel = equipment.level,
            name = equipment.name,
            price = 0,
            type = ItemType.NORMAL,
        )
    }
}

private fun String.onLanguage(ko: String, en: String) = if (this == "ko")
    ko
else
    en

fun Equipment.toItemInfoDomainModel(): com.jinproject.domain.entity.item.Equipment =
    (factory.create(
        equipment = this,
        stats = this.stats.toMutableMap()
    ) as EnchantableEquipment).apply {
        this@apply.enchantNumber = this@toItemInfoDomainModel.enchantNumber
    }

fun List<Equipment>.toItemInfoListDomainModel() = map { it.toItemInfoDomainModel() }

fun EnchantableEquipment.toEquipmentData() = Equipment(
    name = name,
    level = limitedLevel,
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
    enchantNumber = enchantNumber,
    factory = getEquipmentFactory(),
    uuid = uuid,
)
