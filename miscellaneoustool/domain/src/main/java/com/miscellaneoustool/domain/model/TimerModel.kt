package com.miscellaneoustool.domain.model


data class TimerModel(
    val id: Int,
    val bossName: String,
    val day: WeekModel,
    val hour: Int,
    val minutes: Int,
    val seconds: Int,
    val isOverlayOnOrNot: Boolean
) {
    companion object {
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
