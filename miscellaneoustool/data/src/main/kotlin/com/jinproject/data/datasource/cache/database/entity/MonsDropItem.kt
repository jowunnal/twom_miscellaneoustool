package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["mdMonsName", "mdItemName"],
    foreignKeys = [ForeignKey(
        entity = Monster::class,
        parentColumns = arrayOf("monsName"),
        childColumns = arrayOf("mdMonsName")
    ),
        ForeignKey(
            entity = Item::class,
            parentColumns = arrayOf("itemName"),
            childColumns = arrayOf("mdItemName")
        )], indices = [Index("mdItemName")]
)
data class MonsDropItem(
    val mdMonsName: String,
    val mdItemName: String
)