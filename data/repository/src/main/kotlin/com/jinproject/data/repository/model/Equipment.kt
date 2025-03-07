package com.jinproject.data.repository.model

data class Equipment(
    val name: String,
    val level: Int,
    val img_name: String,
)

fun Equipment.toItemInfoDomainModel(stat: Map<String, Float> = emptyMap()) =
    com.jinproject.domain.model.ItemInfo(
        name = name,
        level = level,
        stat = stat,
        imgName = img_name,
        enchantNumber = 0,
        uuid = "0L"
    )

fun List<Equipment>.toItemInfoListDomainModel() = map { it.toItemInfoDomainModel() }
