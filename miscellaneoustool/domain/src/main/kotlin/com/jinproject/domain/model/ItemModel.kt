package com.jinproject.domain.model

data class ItemModel(
    val name: String,
    val count: Int,
    val enchantNumber: Int,
    val price: Long,
    val type: ItemType,
)