package com.jinproject.features.alarm.alarm.mapper

import com.jinproject.domain.model.TimerModel
import com.jinproject.features.alarm.alarm.item.TimeState
import com.jinproject.features.alarm.alarm.item.TimerState

fun TimerModel.toTimerState() = TimerState(
    id = id,
    bossName = bossName,
    ota = isOverlayOnOrNot,
    timeState = TimeState(
        day = day,
        hour = hour,
        minutes = minutes,
        seconds = seconds
    )
)