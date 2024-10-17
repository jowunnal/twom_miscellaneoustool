package com.jinproject.features.alarm.alarm.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.jinproject.design_ui.R
import com.jinproject.features.alarm.alarm.service.ReAlarmService

@SuppressLint("DiscouragedApi")
internal fun NotificationManager.sendNotification(
    name: String,
    img: String,
    code: Int,
    context: Context,
    intervalFirstTimerSetting: Int = 0,
    intervalSecondTimerSetting: Int = 0,
) {
    val backToAlarmIntent = Intent(
        context,
        Class.forName("com.jinproject.twomillustratedbook.ui.MainActivity")
    ).apply {
        putExtra("screen", "alarm")
    }


    val backToAlarmPendingIntent = PendingIntent.getActivity(
        context,
        code,
        backToAlarmIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val reAlarmIntent = Intent(context, ReAlarmService::class.java).apply {
        putExtra("name", name)
        putExtra("code", code)
        putExtra("backToAlarmIntent", backToAlarmIntent)
    }

    val reAlarmPendingIntent = PendingIntent.getService(
        context,
        code,
        reAlarmIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val assetManager = context.assets
    val inputStream = assetManager.open("img/monster/$img.png")
    val bitMap = BitmapFactory.decodeStream(inputStream)

    val builder = NotificationCompat.Builder(context, "TwomBossAlarm")
        .setSmallIcon(IconCompat.createWithBitmap(bitMap))
        .setContentTitle(context.getString(R.string.alarm))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(backToAlarmPendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(bitMap)

    val alarmMessage = StringBuilder()
        .append(context.getString(R.string.alarm_message_head) + name + context.getString(R.string.alarm_message_body))

    if (code < 300) {
        builder.setContentText(
            alarmMessage.append(
                " $intervalFirstTimerSetting ${context.getString(R.string.alarm_message_tail)}"
            ).toString()
        )
    } else {
        builder.setContentText(
            alarmMessage.append(
                " $intervalSecondTimerSetting ${context.getString(R.string.alarm_message_tail)}"
            ).toString()
        )
        builder.addAction(
            R.drawable.img_add_alarm,
            context.getString(R.string.alarm_regeneration),
            reAlarmPendingIntent
        )
    }
    notify(code, builder.build())

}

fun NotificationManager.createChannel(
    context: Context
) {
    val name = context.getString(R.string.channel_name)
    val descriptionText = context.getString(R.string.channel_description)
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel("TwomBossAlarm", name, importance).apply {
        description = descriptionText
        enableVibration(true)
        setShowBadge(true)
        enableLights(true)
        lightColor = Color.BLUE
    }

    createNotificationChannel(channel)
}

internal fun NotificationManager.cancelNotification(id: Int) {
    cancel(id)
}