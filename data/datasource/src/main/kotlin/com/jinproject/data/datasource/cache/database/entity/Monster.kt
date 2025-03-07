package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jinproject.data.repository.model.MonsterModel

@Entity
data class Monster(
    @PrimaryKey val monsName: String,
    val monsLevel: Int,
    val monsGtime: Int,
    val monsImgName: String,
    val monsType: String
)

fun Monster.toMonsterModel() = MonsterModel(
    name = monsName,
    level = monsLevel,
    genTime = monsGtime,
    imgName = monsImgName,
    type = monsType,
    item = emptyList()
)

fun List<Monster>.toItemModels() = map { item -> item.toMonsterModel() }