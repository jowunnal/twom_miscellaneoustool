package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey val itemName: String,
    val itemType: String,
    val itemPrice: Long,
) {
    fun toItemData(): com.jinproject.data.repository.model.Item = com.jinproject.data.repository.model.Item(
        name = itemName,
        price = itemPrice,
        itemType = itemType,
        imageName = ""
    )
}

fun List<Item>.toItemDataModelList(): List<com.jinproject.data.repository.model.Item> = map { it.toItemData() }