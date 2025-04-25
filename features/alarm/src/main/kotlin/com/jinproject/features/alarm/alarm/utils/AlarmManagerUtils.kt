package com.jinproject.features.alarm.alarm.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.jinproject.features.alarm.alarm.receiver.AlarmReceiver

data class AlarmItem(
    val name: String,
    val imgName: String,
    val code: Int
) {
    companion object {
        fun getInitValue() = AlarmItem(
            name = "",
            imgName = "",
            code = -1
        )
    }
}

internal fun AlarmManager.makeAlarm(
    context: Context,
    nextGenTime: Long,
    item: AlarmItem,
    intervalFirstTimerSetting: Int = 0,
    intervalSecondTimerSetting: Int = 0,
) {
    val notifyIntentImmediately = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("name", item.name)
        putExtra("img", item.imgName)
        putExtra("code", item.code)
        putExtra("first", intervalFirstTimerSetting)
        putExtra("second", intervalSecondTimerSetting)
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