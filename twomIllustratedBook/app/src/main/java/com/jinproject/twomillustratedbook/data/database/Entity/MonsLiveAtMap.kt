package com.jinproject.twomillustratedbook.data.database.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["mlMonsName", "mlMapName"],
    foreignKeys = [ForeignKey(
        entity = Monster::class,
        parentColumns = arrayOf("monsName"),
        childColumns = arrayOf("mlMonsName")
    ),
        ForeignKey(
            entity = Map::class,
            parentColumns = arrayOf("mapName"),
            childColumns = arrayOf("mlMapName")
        )], indices = [Index("mlMapName")]
)
data class MonsLiveAtMap(
    val mlMonsName: String,
    val mlMapName: String
)