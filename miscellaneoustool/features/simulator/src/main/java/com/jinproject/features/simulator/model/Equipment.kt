package com.jinproject.features.simulator.model

import com.jinproject.domain.model.Category
import com.jinproject.domain.model.ItemInfo
import com.jinproject.domain.model.Stat
import com.jinproject.domain.model.getItemCategory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

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
internal abstract class Equipment : Item() {
    abstract val level: Int
    abstract val options: List<ItemOption>
    abstract val enchantNumber: Int
    abstract val uuid: String
}

internal fun Equipment.toItemInfoDomainModel(): ItemInfo = when (this) {
    is Weapon -> {
        ItemInfo(
            name = name,
            uuid = uuid,
            imgName = imgName,
            level = level,
            enchantNumber = enchantNumber,
            stat = mutableMapOf<String, Float>().apply {
                options.forEach { option ->
                    put(option.name.name, option.value)
                }
                put(Stat.MINDAMAGE.name, damage.first.toFloat())
                put(Stat.MAXDAMAGE.name, damage.last.toFloat())
                put(Stat.SPEED.name, speed)
            },
        )
    }

    is Armor -> {
        ItemInfo(
            name = name,
            uuid = uuid,
            imgName = imgName,
            level = level,
            enchantNumber = enchantNumber,
            stat = mutableMapOf<String, Float>().apply {
                options.forEach { option ->
                    put(option.name.name, option.value)
                }
                put(Stat.ARMOR.name, armor.toFloat())
            },
        )
    }

    else -> throw IllegalStateException("$this is not allowed Equipment type")
}

internal fun ItemInfo.toEquipment(): Equipment = run {
    when (val type = stat.keys.getItemCategory()) {
        Category.WEAPONS -> {
            val minDamage = Stat.MINDAMAGE.name
            val maxDamage = Stat.MAXDAMAGE.name
            val speed = Stat.SPEED.name

            val damage = (stat[minDamage]?.toInt() ?: 0)..(stat[maxDamage]?.toInt() ?: 1)
            val weaponSpeed = stat[speed] ?: 0f

            Weapon(
                name = name,
                level = level,
                options = mutableListOf<ItemOption>().apply {
                    stat.filter { it.key != minDamage && it.key != maxDamage && it.key != speed }
                        .forEach {
                            add(
                                ItemOption(
                                    name = Stat.valueOf(it.key),
                                    value = it.value
                                )
                            )
                        }
                }.sorted(),
                enchantNumber = enchantNumber,
                imgName = imgName,
                damage = damage,
                speed = weaponSpeed,
                uuid = uuid,
            )
        }

        Category.ARMORS -> {
            val armor = Stat.ARMOR.name

            Armor(
                name = name,
                level = level,
                options = mutableListOf<ItemOption>().apply {
                    stat.filter { it.key != armor }.forEach {
                        add(
                            ItemOption(
                                name = Stat.valueOf(it.key),
                                value = it.value
                            )
                        )
                    }
                }.sorted(),
                enchantNumber = enchantNumber,
                imgName = imgName,
                armor = stat[armor]?.toInt() ?: 0,
                uuid = uuid,
            )
        }

        else -> {
            throw IllegalStateException("$type is not allowed item's type")
        }
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
) : Equipment()

@Serializable
data class ItemOption(
    val name: Stat,
    val value: Float,
) : Comparable<ItemOption> {
    override fun compareTo(other: ItemOption): Int {
        return name.compareTo(other.name)
    }
}