package com.jinproject.data.repository.model

import com.jinproject.domain.usecase.alarm.SetAlarmUsecase
import java.time.ZonedDateTime

data class Timer(
    val timerId: Int,
    val dateTime: ZonedDateTime,
    val ota: Int,
    val monsterName: String,
)

fun Timer.toTimerDomain() = SetAlarmUsecase.Timer(
    id = timerId,
    monsterName = monsterName,
    dateTime = dateTime,
)