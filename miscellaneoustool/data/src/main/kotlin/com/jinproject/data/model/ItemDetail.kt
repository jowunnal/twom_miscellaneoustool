package com.jinproject.data.model

import com.jinproject.domain.model.ItemInfo

data class ItemDetail(
    val name: String,
    val imgName: String,
    val level: Int,
    val stat: Map<String, Float>,
)

fun ItemDetail.toItemInfoDomainModel() = ItemInfo(
    name = name,
    imgName = imgName,
    level = level,
    stat = stat,
    uuid = "0L",
    enchantNumber = 0,
)
