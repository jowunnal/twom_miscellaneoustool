package com.jinproject.twomillustratedbook.data.database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Map(
    @PrimaryKey val mapName: String,
    val mapImgName: String
)