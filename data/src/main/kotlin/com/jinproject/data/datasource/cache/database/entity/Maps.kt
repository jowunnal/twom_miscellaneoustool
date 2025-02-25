package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Maps(
    @PrimaryKey val mapName: String,
    val mapImgName: String
)