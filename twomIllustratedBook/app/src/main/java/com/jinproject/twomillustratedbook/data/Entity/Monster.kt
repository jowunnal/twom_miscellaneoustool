package com.jinproject.twomillustratedbook.data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Monster(
    @PrimaryKey val monsName: String,
    val monsLevel: Int,
    val monsGtime: Int,
    val monsImgName: String,
    val monsType: String
)