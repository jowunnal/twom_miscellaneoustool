package com.jinproject.features.alarm

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.jinproject.features.alarm.service.ReAlarmService
import com.jinproject.features.core.R

@SuppressLint("DiscouragedApi")
fun NotificationManager.sendNotification(
    name: String,
    img: String,
    code: Int,
    context: Context,
    intervalFirstTimerSetting: Int = 0,
    intervalSecondTimerSetting: Int = 0
) {
    //val contentIntent = Intent(context, MainActivity::class.java)
    val alarmIntent = Intent(context, ReAlarmService::class.java).apply {
        putExtra("name", name)
        putExtra("code", code)
    }

    val alarmPendingIntent = PendingIntent.getService(
        context,
        code,
        alarmIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    /*val pendingIntent = PendingIntent.getActivity(
        context,
        code,
        contentIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )*/

    val assetManager = context.assets
    val inputStream = assetManager.open("img/monster/$img.png")
    val bitMap = BitmapFactory.decodeStream(inputStream)

    val builder = NotificationCompat.Builder(context, "TwomBossAlarm")
        .setSmallIcon(IconCompat.createWithBitmap(bitMap))
        .setContentTitle("알람")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        //.setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(bitMap)

    val alarmMessage = StringBuilder()
        .append(context.getString(R.string.alarm_message_head) + name + context.getString(R.string.alarm_message_body))

    if (code < 300) {
        builder.setContentText(alarmMessage.append(intervalFirstTimerSetting.toString() + context.getString(R.string.alarm_message_tail)).toString())
    } else {
        builder.setContentText(alarmMessage.append(intervalSecondTimerSetting.toString() + context.getString(R.string.alarm_message_tail)).toString())
        builder.addAction(R.drawable.img_add_alarm, context.getString(R.string.alarm_regeneration), alarmPendingIntent)
    }
    notify(code, builder.build())

}

fun NotificationManager.cancelNotification(id: Int) {
    cancel(id)
}