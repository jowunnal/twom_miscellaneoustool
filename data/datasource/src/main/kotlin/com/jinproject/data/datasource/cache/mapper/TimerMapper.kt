package com.jinproject.data.datasource.cache.mapper

import com.jinproject.core.TimerPreferences
import com.jinproject.data.repository.model.TimerSetting

fun TimerPreferences.toTimerSetting() = TimerSetting(
    fontSize = fontSize,
    xPos = xPos,
    yPos = yPos,
    frequentlyUsedBossList = frequentlyUsedBossListList,
    firstInterval = intervalFirstTimerSetting,
    secondInterval = intervalSecondTimerSetting,
    overlaidMonsterList = overlayBossListList
)