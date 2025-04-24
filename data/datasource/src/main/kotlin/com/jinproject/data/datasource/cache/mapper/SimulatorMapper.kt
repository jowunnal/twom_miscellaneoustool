package com.jinproject.data.datasource.cache.mapper

import com.jinproject.data.ItemInfo
import com.jinproject.data.datasource.cache.database.dao.ItemWithEquipmentInfo
import com.jinproject.data.repository.model.Equipment
import com.jinproject.data.repository.model.EquipmentEntity
import com.jinproject.data.repository.model.EquipmentInfo

fun Equipment.toItemInfo(): ItemInfo = ItemInfo.newBuilder()
    .setName(info.name)
    .setEnchantNumber(info.enchantNumber)
    .setUuid(info.uuid)
    .putAllOptions(info.stats)
    .build()

fun ItemInfo.toEquipmentProto(): EquipmentInfo = EquipmentInfo(
    name = name,
    stats = optionsMap,
    enchantNumber = enchantNumber,
    uuid = uuid,
)

fun List<ItemInfo>.toEquipmentProtoList(): List<EquipmentInfo> = map { it.toEquipmentProto() }

fun Map<ItemWithEquipmentInfo, List<com.jinproject.data.datasource.cache.database.entity.ItemInfo>>.toEquipmentEntities(): List<EquipmentEntity> =
    map { entries ->
        entries.toEquipmentEntity()
    }

fun Map.Entry<ItemWithEquipmentInfo, List<com.jinproject.data.datasource.cache.database.entity.ItemInfo>>.toEquipmentEntity(): EquipmentEntity =
    EquipmentEntity(
        level = key.level,
        itemType = key.itemType,
        imageName = key.img_name,
        price = key.itemPrice,
    )