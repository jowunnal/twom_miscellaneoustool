package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["item_name", "type"],
    foreignKeys = [ForeignKey(
        entity = Item::class,
        parentColumns = ["itemName"],
        childColumns = ["item_name"]
    )]
)
data class ItemInfo(
    val item_name: String,
    val type: String,
    val value: Double
)
