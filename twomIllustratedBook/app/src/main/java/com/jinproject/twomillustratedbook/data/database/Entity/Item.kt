package com.jinproject.twomillustratedbook.data.database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey val itemName: String,
    val itemType: String
)