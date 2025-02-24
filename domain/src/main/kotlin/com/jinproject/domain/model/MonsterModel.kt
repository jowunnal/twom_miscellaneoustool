package com.jinproject.domain.model

data class MonsterModel(
    val name: String,
    val level: Int,
    val genTime: Int,
    val imgName: String,
    val type: MonsterType,
    val item: List<ItemModel>
)
