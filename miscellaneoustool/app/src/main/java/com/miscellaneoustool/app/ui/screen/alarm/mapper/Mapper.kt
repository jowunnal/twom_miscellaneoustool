package com.miscellaneoustool.app.ui.screen.alarm.mapper

import com.miscellaneoustool.app.ui.screen.alarm.item.TimeState
import com.miscellaneoustool.app.ui.screen.alarm.item.TimerState
import com.miscellaneoustool.domain.model.TimerModel

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