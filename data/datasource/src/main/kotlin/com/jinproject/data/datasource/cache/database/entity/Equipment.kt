package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Item::class,
        parentColumns = ["itemName"],
        childColumns = ["name"]
    )]
)
data class Equipment(
    @PrimaryKey val name: String,
    val level: Int,
    val img_name: String,
)

fun Equipment.toEquipmentDataModel() = com.jinproject.data.repository.model.Equipment(
    name = name,
    level = level,
    img_name = img_name,
)

fun List<Equipment>.toEquipmentsDataModel() = map { it.toEquipmentDataModel() }