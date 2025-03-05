package com.jinproject.data.repository.model

import com.jinproject.domain.model.TimerModel
import com.jinproject.domain.model.WeekModel

data class Timer(
    val timerId: Int,
    val day: Int,
    val hour: Int,
    val min: Int,
    val sec: Int,
    val ota: Int,
    val timerMonsName: String
)

fun Timer.toTimerModel() = TimerModel(
    id = timerId,
    bossName = timerMonsName,
    day = WeekModel.findByCode(day),
    hour = hour,
    minutes = min,
    seconds = sec,
    isOverlayOnOrNot = ota == 1
)