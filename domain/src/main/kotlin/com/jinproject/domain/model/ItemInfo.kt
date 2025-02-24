package com.jinproject.domain.model

data class ItemInfo(
    val name: String,
    val uuid: String,
    val imgName: String,
    val level: Int,
    val enchantNumber: Int,
    val stat: Map<String, Float>,
)