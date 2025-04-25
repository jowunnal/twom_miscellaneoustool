package com.jinproject.features.alarm.alarm.item

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.jinproject.domain.usecase.alarm.SetAlarmUsecase
import kotlinx.parcelize.Parcelize
import java.time.ZoneId
import java.time.ZonedDateTime

@Stable
@Parcelize
data class TimerState(
    val id: Int,
    val bossName: String,
    val dateTime: ZonedDateTime,
) : Parcelable {
    companion object {
        fun getInitValue() = TimerState(
            id = 0,
            bossName = "",
            dateTime = ZonedDateTime.now()
        )

        fun fromDomain(domain: SetAlarmUsecase.Timer) = TimerState(
            id = domain.id,
            bossName = domain.monsterName,
            dateTime = domain.dateTime,
        )
    }
}