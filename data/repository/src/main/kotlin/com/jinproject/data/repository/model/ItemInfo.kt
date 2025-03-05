package com.jinproject.data.repository.model

data class ItemInfo(
    val name: String,
    val uuid: String,
    val enchantNumber: Int,
    val stat: Map<String, Float>,
)

fun com.jinproject.domain.model.ItemInfo.fromDomain(): ItemInfo = ItemInfo(
    name = name,
    uuid = uuid,
    enchantNumber = enchantNumber,
    stat = stat,
)