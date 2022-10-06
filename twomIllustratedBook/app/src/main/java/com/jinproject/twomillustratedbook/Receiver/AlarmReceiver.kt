package com.jinproject.twomillustratedbook.Receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import com.jinproject.twomillustratedbook.utils.sendNotification
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.Service.AlarmService

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        val notificationManager = p0!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(p0)
        val msg=p1!!.getStringExtra("msg")!!
        val img=p1.getStringExtra("img")!!
        val code=p1.getIntExtra("code",0)
        val gtime=p1.getIntExtra("gtime",0)
        notificationManager.sendNotification(msg,img,code,p0,gtime)
        if(code>300){
            notificationManager.sendNotification(msg,img,code,p0,gtime)
            val intent=Intent(p0, AlarmService::class.java).apply { putExtra("name",msg)}
            p0.startService(intent)
        }

    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("TwomBossAlarm", name, importance).apply {
                description = descriptionText
                enableVibration(true)
                setShowBadge(true)
                enableLights(true)
                lightColor= Color.BLUE
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}