package com.jinproject.data.repository.model

data class AlarmStoredBoss(
    val list: List<String>,
    val classified: String,
    val name: String
)

fun AlarmStoredBoss.toDomainModel() = com.jinproject.domain.usecase.timer.AlarmStoredBoss(
    list = list,
    classified = classified,
    name = name,
)

data class Interval(
    val first: Int,
    val second: Int
)

fun Interval.toDomainModel() = com.jinproject.domain.usecase.timer.Interval(
    first = first,
    second = second,
)

data class OverlaySetting(
    val fontSize: Int,
    val xPos: Int,
    val yPos: Int,
    val frequentlyUsedBossList: List<String>
)

fun OverlaySetting.toDomainModel() = com.jinproject.domain.usecase.timer.OverlaySetting(
    fontSize = fontSize,
    xPos = xPos,
    yPos = yPos,
    frequentlyUsedBossList = frequentlyUsedBossList,
)