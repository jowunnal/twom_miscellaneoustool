package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["mlMonsName", "mlMapName"],
    foreignKeys = [
        ForeignKey(
            entity = Monster::class,
            parentColumns = arrayOf("monsName"),
            childColumns = arrayOf("mlMonsName")
        ),
        ForeignKey(
            entity = Maps::class,
            parentColumns = arrayOf("mapName"),
            childColumns = arrayOf("mlMapName"),
            onUpdate = ForeignKey.CASCADE,
        )
    ],
)
data class MonsLiveAtMap(
    val mlMonsName: String,
    val mlMapName: String
)