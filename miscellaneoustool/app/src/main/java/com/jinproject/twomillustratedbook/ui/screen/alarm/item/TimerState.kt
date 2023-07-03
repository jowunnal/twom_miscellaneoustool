package com.jinproject.twomillustratedbook.ui.screen.alarm.item

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class TimerState(
    val id: Int,
    val bossName: String,
    val ota: Boolean = false,
    val timeState: TimeState
) : Parcelable {
    companion object {
        fun getInitValue() = TimerState(
            id = 0,
            bossName = "",
            ota = false,
            timeState = TimeState.getInitValue()
        )
    }
}