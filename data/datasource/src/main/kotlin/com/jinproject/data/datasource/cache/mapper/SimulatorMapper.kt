package com.jinproject.data.datasource.cache.mapper

import com.jinproject.data.ItemInfo
import com.jinproject.data.datasource.cache.database.dao.ItemWithEquipmentInfo
import com.jinproject.data.datasource.cache.database.entity.associateToStat
import com.jinproject.data.repository.model.AccessoryFactory
import com.jinproject.data.repository.model.ArmorFactory
import com.jinproject.data.repository.model.Equipment
import com.jinproject.data.repository.model.WeaponFactory

fun Equipment.toItemInfo(): ItemInfo = ItemInfo.newBuilder()
    .setName(name)
    .setEnchantNumber(enchantNumber)
    .setUuid(uuid)
    .putAllOptions(stats)
    .build()

fun ItemInfo.toEquipment(): Equipment = Equipment(
    name = name,
    level = 0,
    stats = optionsMap,
    enchantNumber = enchantNumber,
    factory = WeaponFactory,
    uuid = uuid,
)

fun List<ItemInfo>.toEquipments(): List<Equipment> = map { it.toEquipment() }

fun Map<ItemWithEquipmentInfo, List<com.jinproject.data.datasource.cache.database.entity.ItemInfo>>.toEquipments(): List<Equipment> =
    map { entries ->
        entries.toEquipment()
    }

fun Map.Entry<ItemWithEquipmentInfo, List<com.jinproject.data.datasource.cache.database.entity.ItemInfo>>.toEquipment(): Equipment =
    Equipment(
        name = key.itemName,
        level = key.level,
        stats = value.associateToStat(),
        enchantNumber = 0,
        factory = key.itemType.itemTypeToEquipmentDomainFactory(),
        uuid = ""
    )

internal fun String.itemTypeToEquipmentDomainFactory(): com.jinproject.data.repository.model.EquipmentFactory =
    when (this) {
        "무기", "weapon" -> WeaponFactory
        "방어구", "armor" -> ArmorFactory
        "장신구", "accessories" -> AccessoryFactory
        "코스튬", "costume" -> ArmorFactory
        else -> throw IllegalArgumentException("[$this] 는 장비 아이템 타입이 아닙니다.")
    }