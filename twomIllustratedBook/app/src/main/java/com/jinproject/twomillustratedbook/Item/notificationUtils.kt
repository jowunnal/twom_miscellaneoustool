package com.jinproject.twomillustratedbook.Item

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import com.jinproject.twomillustratedbook.Fragment.Alarm
import com.jinproject.twomillustratedbook.MainActivity.MainActivity
import com.jinproject.twomillustratedbook.R

fun NotificationManager.sendNotification(message:String,img:String,code:Int,applicationContext: Context){
    val contentIntent= Intent(applicationContext,MainActivity::class.java)
    val pendingIntent=PendingIntent.getActivity(applicationContext,code+600,contentIntent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    val res=applicationContext.resources.getIdentifier(img,"drawable",applicationContext.packageName)
    val builder= NotificationCompat.Builder(applicationContext,"TwomBossAlarm")
        .setSmallIcon(res)
        .setContentTitle("알람")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources,res))
    if(code<300){
        builder.setContentText("띵동~ <"+message+"> 젠타임 5분전 입니다.")
    }else{
        builder.setContentText("띵동~ <"+message+"> 젠타임 입니다.")
    }
    notify(code,builder.build())
}

fun NotificationManager.cancelNotification(){
    cancelAll()
}