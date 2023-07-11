package com.jinproject.twomillustratedbook.utils

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.MainActivity
import com.jinproject.twomillustratedbook.ui.service.ReAlarmService

@SuppressLint("DiscouragedApi")
fun NotificationManager.sendNotification(
    message: String,
    img: String,
    code: Int,
    applicationContext: Context,
    gtime: Int,
    intervalFirstTimerSetting: Int = 0,
    intervalSecondTimerSetting: Int = 0
) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val alarmIntent = Intent(applicationContext, ReAlarmService::class.java).apply {
        putExtra("msg", message)
        putExtra("img", img)
        putExtra("code", code)
        putExtra("gtime", gtime)
    }

    val alarmPendingIntent = PendingIntent.getService(
        applicationContext,
        code,
        alarmIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        code,
        contentIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val assetManager = applicationContext.assets
    val inputStream = assetManager.open("img/monster/$img.png")
    val bitMap = BitmapFactory.decodeStream(inputStream)

    val builder = NotificationCompat.Builder(applicationContext, "TwomBossAlarm")
        .setSmallIcon(IconCompat.createWithBitmap(bitMap))
        .setContentTitle("알람")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(bitMap)

    val alarmMessage = StringBuilder()
        .append(applicationContext.getString(R.string.alarm_message_head) + message + applicationContext.getString(R.string.alarm_message_body))

    if (code < 300) {
        builder.setContentText(alarmMessage.append(intervalFirstTimerSetting.toString() + applicationContext.getString(R.string.alarm_message_tail)).toString())
    } else {
        builder.setContentText(alarmMessage.append(intervalSecondTimerSetting.toString() + applicationContext.getString(R.string.alarm_message_tail)).toString())
        builder.addAction(R.drawable.img_add_alarm, applicationContext.getString(R.string.alarm_regeneration), alarmPendingIntent)
    }
    notify(code, builder.build())

}

fun NotificationManager.cancelNotification(id: Int) {
    cancel(id)
}