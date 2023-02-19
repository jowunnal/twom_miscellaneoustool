package com.jinproject.twomillustratedbook.domain.model

import android.os.health.TimerStat
import androidx.compose.runtime.Stable
import com.jinproject.twomillustratedbook.data.database.Entity.Timer
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimeState
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimerState

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
