package com.jinproject.data.repository.model

import com.jinproject.domain.usecase.alarm.ManageTimerSettingUsecase

data class TimerSetting(
    val fontSize: Int?,
    val xPos: Int?,
    val yPos: Int?,
    val frequentlyUsedBossList: List<String>?,
    val firstInterval: Int?,
    val secondInterval: Int?,
    val overlaidMonsterList: List<String>?,
) {
    fun toDomain(): ManageTimerSettingUsecase.TimerSetting = ManageTimerSettingUsecase.TimerSetting(
        fontSize = fontSize,
        xPos = xPos,
        yPos = yPos,
        frequentlyUsedBossList = frequentlyUsedBossList,
        interval = ManageTimerSettingUsecase.TimerInterval(
            firstInterval = firstInterval,
            secondInterval = secondInterval,
        ),
        overlaidMonsterList = overlaidMonsterList,
    )

    companion object {
        fun fromDomain(timerSetting: ManageTimerSettingUsecase.TimerSetting) = TimerSetting(
            fontSize = timerSetting.fontSize,
            xPos = timerSetting.xPos,
            yPos = timerSetting.yPos,
            frequentlyUsedBossList = timerSetting.frequentlyUsedBossList,
            firstInterval = timerSetting.interval?.firstInterval,
            secondInterval = timerSetting.interval?.secondInterval,
            overlaidMonsterList = timerSetting.overlaidMonsterList,
        )
    }
}