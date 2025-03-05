package com.jinproject.data.datasource.cache.mapper

import com.jinproject.core.TimerPreferences
import com.jinproject.data.repository.model.AlarmStoredBoss
import com.jinproject.data.repository.model.Interval
import com.jinproject.data.repository.model.OverlaySetting

fun TimerPreferences.toOverlaySetting() = OverlaySetting(
    fontSize = fontSize,
    xPos = xPos,
    yPos = yPos,
    frequentlyUsedBossList = frequentlyUsedBossListList
)

fun TimerPreferences.toInterval() = Interval(
    first = intervalFirstTimerSetting,
    second = intervalSecondTimerSetting
)

fun TimerPreferences.toAlarmStoredBoss() = AlarmStoredBoss(
    list = frequentlyUsedBossListList,
    classified = recentlySelectedBossClassified,
    name = recentlySelectedBossName
)