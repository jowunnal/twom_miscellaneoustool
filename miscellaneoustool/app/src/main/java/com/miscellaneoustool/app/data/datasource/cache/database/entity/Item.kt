package com.miscellaneoustool.app.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey val itemName: String,
    val itemType: String,
    val itemPrice: Int
)