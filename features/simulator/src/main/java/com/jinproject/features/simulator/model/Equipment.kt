package com.jinproject.features.simulator.model

import com.jinproject.domain.entity.item.EnchantableEquipment
import com.jinproject.domain.entity.item.Scroll
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal val formatter
    get() = Json {
        serializersModule = SerializersModule {
            polymorphic(Item::class) {
                subclass(Weapon::class)
                subclass(Armor::class)
                subclass(Empty::class)
                subclass(EnchantScroll.WeaponS::class)
                subclass(EnchantScroll.WeaponA::class)
                subclass(EnchantScroll.WeaponB::class)
                subclass(EnchantScroll.WeaponC::class)
                subclass(EnchantScroll.WeaponD::class)
            }

            polymorphic(EnchantScroll::class) {
                subclass(EnchantScroll.WeaponS::class)
                subclass(EnchantScroll.WeaponA::class)
                subclass(EnchantScroll.WeaponB::class)
                subclass(EnchantScroll.WeaponC::class)
                subclass(EnchantScroll.WeaponD::class)
            }

            polymorphic(Equipment::class) {
                subclass(Weapon::class)
                subclass(Armor::class)
                subclass(Empty::class)
            }
        }
        encodeDefaults = true
        classDiscriminator = "Item"
    }


@Serializable
internal abstract class Item {
    abstract val name: String
    abstract val imgName: String
}

@Serializable
internal abstract class Equipment : Item(), EnchantableEquipmentDomainFactory {
    abstract val level: Int
    abstract val options: List<ItemOption>
    abstract val enchantNumber: Int
    abstract val uuid: String
}

internal interface EnchantableEquipmentDomainFactory {
    fun toDomainModel(uuid: String? = null): EnchantableEquipment
}

internal fun Equipment.toDomain(uuid: String? = null) = toDomainModel(uuid).apply {
    enchantNumber = this@toDomain.enchantNumber
}

internal fun com.jinproject.domain.entity.item.Equipment.fromDomainModel(uuid: String? = null): Equipment {
    return when (this) {
        is com.jinproject.domain.entity.item.Weapon -> {
            Weapon(
                name = name,
                level = limitedLevel,
                options = stats.map {
                    ItemOption(
                        name = it.key,
                        value = it.value
                    )
                }.sorted(),
                enchantNumber = enchantNumber,
                imgName = imageName,
                damage = damageRange,
                speed = speed.toFloat(),
                uuid = uuid ?: this.uuid,
            )
        }

        is com.jinproject.domain.entity.item.Armor -> {
            Armor(
                name = name,
                level = limitedLevel,
                options = stats.map {
                    ItemOption(
                        name = it.key,
                        value = it.value
                    )
                }.sorted(),
                enchantNumber = enchantNumber,
                imgName = imageName,
                armor = armor,
                uuid = uuid ?: this.uuid,
            )
        }

        else -> throw IllegalStateException()
    }
}

@Serializable
internal data class Empty(
    override val name: String = "",
    override val level: Int = 0,
    override val options: List<ItemOption> = emptyList(),
    override val enchantNumber: Int = 0,
    override val uuid: String = "-1L",
    override val imgName: String = ""
) : Equipment() {
    @OptIn(ExperimentalUuidApi::class)
    override fun toDomainModel(uuid: String?): EnchantableEquipment {
        return EmptyEnchantableEquipment(uuid = uuid ?: Uuid.random().toString())
    }

    data class EmptyEnchantableEquipment constructor(
        override val name: String = "",
        override var enchantNumber: Int = 0,
        override val enchantProbability: Int = 0,
        override val stats: Map<String, Float> = emptyMap(),
        override val limitedLevel: Int = 0,
        override val type: com.jinproject.domain.entity.item.ItemType = com.jinproject.domain.entity.item.ItemType.NORMAL,
        override val price: Long = 0,
        override val uuid: String,
        override val imageName: String = "burning_blade",
    ) : EnchantableEquipment() {
        override fun isAvailableScroll(scroll: Scroll): Boolean {
            return false
        }

        override fun enchant(): com.jinproject.domain.entity.item.Equipment? {
            return null
        }
    }
}

@Serializable
data class ItemOption(
    val name: String,
    val value: Float,
) : Comparable<ItemOption> {
    override fun compareTo(other: ItemOption): Int {
        return name.compareTo(other.name)
    }
}

fun List<ItemOption>.toMap(): Map<String, Float> =
    associate { it.name to it.value }