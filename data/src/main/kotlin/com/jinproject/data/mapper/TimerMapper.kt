package com.jinproject.data.mapper

import com.jinproject.core.TimerPreferences
import com.jinproject.domain.model.TimerModel
import com.jinproject.domain.model.WeekModel
import com.jinproject.domain.usecase.timer.AlarmStoredBoss
import com.jinproject.domain.usecase.timer.Interval
import com.jinproject.domain.usecase.timer.OverlaySetting

fun com.jinproject.data.datasource.cache.database.entity.Timer.toTimerModel() = TimerModel(
    id = timerId,
    bossName = timerMonsName,
    day = WeekModel.findByCode(day),
    hour = hour,
    minutes = min,
    seconds = sec,
    isOverlayOnOrNot = ota == 1
)

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