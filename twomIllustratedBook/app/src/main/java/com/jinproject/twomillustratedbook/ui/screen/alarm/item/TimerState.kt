package com.jinproject.twomillustratedbook.ui.screen.alarm.item

import androidx.compose.runtime.Stable

@Stable
data class TimerState(
    val id: Int,
    val bossName: String,
    val timeState: TimeState
) {
    companion object {
        fun getInitValue() = TimerState(
            id = 0,
            bossName = "",
            timeState = TimeState.getInitValue()
        )
    }
}