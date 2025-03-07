package com.jinproject.data.datasource.cache.mapper

import com.jinproject.data.ItemInfo

fun ItemInfo.toItemInfoDataModel() = com.jinproject.data.repository.model.ItemInfo(
    name = name,
    uuid = uuid,
    enchantNumber = enchantNumber,
    stat = optionsMap,
)

fun List<ItemInfo>.toItemInfosDataModel() = map { it.toItemInfoDataModel() }

fun com.jinproject.data.repository.model.ItemInfo.toItemInfo(): ItemInfo = ItemInfo.newBuilder().apply {
    name = this@toItemInfo.name
    uuid = this@toItemInfo.uuid
    enchantNumber = this@toItemInfo.enchantNumber
    putAllOptions(this@toItemInfo.stat)
}.build()