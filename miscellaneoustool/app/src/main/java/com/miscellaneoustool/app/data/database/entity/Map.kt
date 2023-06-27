package com.miscellaneoustool.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Map(
    @PrimaryKey val mapName: String,
    val mapImgName: String
)