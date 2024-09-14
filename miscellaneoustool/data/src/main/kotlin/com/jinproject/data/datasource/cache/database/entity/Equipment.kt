package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.jinproject.domain.model.ItemInfo

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

fun Equipment.toItemInfoDomainModel(stat: Map<String, Float> = emptyMap()) = ItemInfo(
    name = name,
    level = level,
    stat = stat,
    imgName = img_name,
    enchantNumber = 0,
    uuid = "0L"
)

fun List<Equipment>.toItemInfoListDomainModel() = map { it.toItemInfoDomainModel() }