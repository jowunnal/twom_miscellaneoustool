package com.jinproject.domain.model

import java.time.LocalDateTime

data class TimerModel(
    val id: Int,
    val bossName: String,
    val dateTime: LocalDateTime,
    val isOverlaying: Boolean
) {

    companion object {
        fun getInitValue() = TimerModel(
            id = 0,
            bossName = "",
            dateTime = LocalDateTime.now(),
            isOverlaying = false
        )
    }
}
