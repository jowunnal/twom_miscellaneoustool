package com.miscellaneoustool.app.domain.model

import androidx.compose.runtime.Stable
import com.miscellaneoustool.app.data.database.entity.Timer
import com.miscellaneoustool.app.ui.screen.alarm.item.TimeState
import com.miscellaneoustool.app.ui.screen.alarm.item.TimerState

@Stable
data class TimerModel(
    val id: Int,
    val bossName: String,
    val day: WeekModel,
    val hour: Int,
    val minutes: Int,
    val seconds: Int,
    val isOverlayOnOrNot: Boolean
) {
    fun toTimerState() = TimerState(
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
    companion object {
        fun fromTimerResponse(response: Timer) = TimerModel(
            id = response.timerId,
            bossName = response.timerMonsName,
            day = WeekModel.findByCode(response.day),
            hour = response.hour,
            minutes = response.min,
            seconds = response.sec,
            isOverlayOnOrNot = response.ota == 1
        )

        fun getInitValue() = TimerModel(
            id = 0,
            bossName = "",
            day = WeekModel.Mon,
            hour = 0,
            minutes = 0,
            seconds = 0,
            isOverlayOnOrNot = false
        )
    }
}
