package com.jinproject.data.repository.model

import com.jinproject.core.util.onLanguage
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
import com.jinproject.domain.entity.item.Item
import com.jinproject.domain.entity.item.ItemType
import com.jinproject.domain.entity.item.Weapon
import java.util.Locale

data class Equipment(
    val info: EquipmentInfo,
    val data: EquipmentEntity,
) : GetItemDomainFactory {
    fun toDomain(): com.jinproject.domain.entity.item.Equipment {
        val factory = getDomainFactory()

        return (factory.create() as EnchantableEquipment).apply {
            this@apply.enchantNumber = this@Equipment.info.enchantNumber
        }
    }

    override fun getDomainFactory(): ItemDomainFactory =
        when (data.itemType) {
            WEAPON_KO, WEAPON_EN -> WeaponDomainFactory(
                stats = info.stats.toMutableMap(),
                equipment = this
            )

            ARMOR_KO, ARMOR_EN -> ArmorDomainFactory(
                stats = info.stats.toMutableMap(),
                equipment = this
            )

            ACCESSORY_KO, ACCESSORY_EN -> AccessoryDomainFactory(
                stats = info.stats.toMutableMap(),
                equipment = this
            )

            COSTUME_KO, COSTUME_EN -> ArmorDomainFactory(
                stats = info.stats.toMutableMap(),
                equipment = this
            )

            else -> throw IllegalArgumentException("[$this] 는 장비 아이템 타입이 아닙니다.")
        }

    companion object {
        fun toItemType(
            equipment: com.jinproject.domain.entity.item.Equipment,
            language: String = Locale.getDefault().language
        ) = when (equipment) {
            is Weapon -> language.onLanguage(ko = WEAPON_KO, en = WEAPON_EN)
            is Armor -> language.onLanguage(ko = ARMOR_KO, en = ARMOR_EN)
            is Accessory -> language.onLanguage(ko = ACCESSORY_KO, en = ACCESSORY_EN)
            is Costume -> language.onLanguage(ko = COSTUME_KO, en = COSTUME_EN)
            else -> throw IllegalArgumentException("[$this] 는 강화 가능한 장비 아이템 타입이 아닙니다.")
        }

        const val WEAPON_KO = "무기"
        const val WEAPON_EN = "weapon"
        const val ARMOR_KO = "방어구"
        const val ARMOR_EN = "armor"
        const val ACCESSORY_KO = "장신구"
        const val ACCESSORY_EN = "accessories"
        const val COSTUME_KO = "코스튬"
        const val COSTUME_EN = "costume"
    }
}

fun interface GetItemDomainFactory {
    fun getDomainFactory(): ItemDomainFactory
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
    val price: Long,
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
            price = 0,
        )
    }
}

abstract class ItemDomainFactory {
    abstract fun create(): Item
}

abstract class EquipmentDomainFactory() : ItemDomainFactory() {
    abstract val equipment: Equipment
    protected val language: String = Locale.getDefault().language
    abstract val stats: MutableMap<String, Float>

    companion object {
        fun EnchantableEquipment.getEquipmentFactory(): EquipmentDomainFactory = when (this) {
            is Weapon -> WeaponDomainFactory(
                stats = stats.toMutableMap(),
                equipment = this.toEquipmentData()
            )

            is Armor -> ArmorDomainFactory(
                stats = stats.toMutableMap(),
                equipment = this.toEquipmentData()
            )

            is Accessory -> AccessoryDomainFactory(
                stats = stats.toMutableMap(),
                equipment = this.toEquipmentData()
            )

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

class WeaponDomainFactory(
    override val stats: MutableMap<String, Float>,
    override val equipment: Equipment
) : EquipmentDomainFactory() {
    override fun create(): com.jinproject.domain.entity.item.Equipment {
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
            price = equipment.data.price,
            type = ItemType.NORMAL,
            speed = speed,
            damageRange = minDamage..maxDamage,
            uuid = equipment.info.uuid,
            imageName = equipment.data.imageName,
        ).apply {
            enchantNumber = equipment.info.enchantNumber
        }
    }
}

class ArmorDomainFactory(
    override val stats: MutableMap<String, Float>,
    override val equipment: Equipment
) : EquipmentDomainFactory() {
    override fun create(): com.jinproject.domain.entity.item.Equipment {
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
            price = equipment.data.price,
            type = ItemType.NORMAL,
            armor = armor,
            uuid = equipment.info.uuid,
            imageName = equipment.data.imageName,
        ).apply {
            enchantNumber = equipment.info.enchantNumber
        }
    }
}

class AccessoryDomainFactory(
    override val stats: MutableMap<String, Float>,
    override val equipment: Equipment
) : EquipmentDomainFactory() {
    override fun create(): com.jinproject.domain.entity.item.Equipment {
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
            price = equipment.data.price,
            type = ItemType.NORMAL,
            armor = armor,
            uuid = equipment.info.uuid,
            imageName = equipment.data.imageName,
        ).apply {
            enchantNumber = equipment.info.enchantNumber
        }
    }
}

class CostumeDomainFactory(
    override val stats: MutableMap<String, Float>,
    override val equipment: Equipment
) : EquipmentDomainFactory() {
    override fun create(): com.jinproject.domain.entity.item.Equipment {
        return Costume(
            stats = equipment.info.stats,
            limitedLevel = equipment.data.level,
            name = equipment.info.name,
            price = equipment.data.price,
            type = ItemType.NORMAL,
            imageName = equipment.data.imageName,
        )
    }
}

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
        price = price,
    ),
)
