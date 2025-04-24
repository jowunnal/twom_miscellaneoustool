package com.jinproject.data.datasource.cache.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = Monster::class,
        parentColumns = arrayOf("monsName"),
        childColumns = arrayOf("timerMonsName")
    )], indices = [Index("timerMonsName")]
)
data class Timer(
    @PrimaryKey(autoGenerate = true) var timerId: Int,
    var epochMilli: Long,
    var ota: Int,
    var timerMonsName: String,
)

fun Timer.toTimerDataModel() = com.jinproject.data.repository.model.Timer(
    timerId = timerId,
    dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault()),
    ota = ota,
    monsterName = timerMonsName,
)