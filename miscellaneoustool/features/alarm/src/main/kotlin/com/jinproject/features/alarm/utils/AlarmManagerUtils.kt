package com.jinproject.features.alarm.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.jinproject.features.alarm.item.AlarmItem
import com.jinproject.features.alarm.receiver.AlarmReceiver

internal fun AlarmManager.makeAlarm(
    context: Context,
    nextGenTime: Long,
    item: AlarmItem,
    intervalFirstTimerSetting: Int = 0,
    intervalSecondTimerSetting: Int = 0,
    backToAlarmIntent: Intent?
) {
    val notifyIntentImmediately = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("name", item.name)
        putExtra("img", item.imgName)
        putExtra("code", item.code)
        putExtra("first", intervalFirstTimerSetting)
        putExtra("second", intervalSecondTimerSetting)
        backToAlarmIntent?.let { putExtra("backToAlarmIntent", it) }
    }

    val notifyPendingIntent = PendingIntent.getBroadcast(
        context,
        item.code,
        notifyIntentImmediately,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    setAlarmClock(
        AlarmManager.AlarmClockInfo(
            nextGenTime,
            notifyPendingIntent
        ), notifyPendingIntent
    )
}