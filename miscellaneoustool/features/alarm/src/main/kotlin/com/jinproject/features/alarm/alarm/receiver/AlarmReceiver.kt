package com.jinproject.features.alarm.alarm.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jinproject.core.util.getParcelableExtraOnVersion
import com.jinproject.features.alarm.alarm.utils.createChannel
import com.jinproject.features.alarm.alarm.utils.sendNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject lateinit var timerRepository: com.jinproject.domain.repository.TimerRepository
    override fun onReceive(p0: Context?, p1: Intent?) {
        val notificationManager =
            p0!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createChannel(p0)

        val name = p1!!.getStringExtra("name")!!
        val img = p1.getStringExtra("img")!!
        val code = p1.getIntExtra("code", 0)
        val intervalFirstTimeSetting = p1.getIntExtra("first",0)
        val intervalSecondTimeSetting = p1.getIntExtra("second",0)
        val backToAlarmIntent = p1.getParcelableExtraOnVersion<Intent>(key = "backToAlarmIntent")

        if (code > 300) {
            notificationManager.sendNotification(
                name = name,
                img = img,
                code = code,
                context = p0,
                intervalSecondTimerSetting = intervalSecondTimeSetting,
                backToAlarmIntent = backToAlarmIntent
            )
            CoroutineScope(Dispatchers.IO).launch {
                timerRepository.deleteTimer(name)
            }
        }
        else {
            notificationManager.sendNotification(
                name = name,
                img = img,
                code = code,
                context = p0,
                intervalFirstTimerSetting = intervalFirstTimeSetting,
                backToAlarmIntent = backToAlarmIntent
            )
        }
    }
}