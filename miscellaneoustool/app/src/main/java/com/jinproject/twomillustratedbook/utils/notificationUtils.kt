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

    if (code < 300) {
        builder.setContentText("띵동~ <" + message + "> 젠타임" + intervalFirstTimerSetting + "분전 입니다.")
    } else {
        builder.setContentText("띵동~ <" + message + "> 젠타임" + intervalSecondTimerSetting + "분전 입니다.")
        builder.addAction(R.drawable.img_add_alarm, "알람생성", alarmPendingIntent)
    }
    notify(code, builder.build())

}

fun NotificationManager.cancelNotification(id: Int) {
    cancel(id)
}