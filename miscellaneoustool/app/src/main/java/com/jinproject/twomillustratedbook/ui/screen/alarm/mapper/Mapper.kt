package com.jinproject.twomillustratedbook.ui.screen.alarm.mapper

import com.jinproject.domain.model.TimerModel
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimeState
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimerState

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