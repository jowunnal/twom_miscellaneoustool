package com.jinproject.twomillustratedbook.ui.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.data.repository.TimerRepository
import com.jinproject.twomillustratedbook.utils.sendNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService : LifecycleService() {
    @Inject lateinit var timerRepository: TimerRepository
    private var notificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        notificationManager =
            applicationContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val msg = intent!!.getStringExtra("msg")!!
        val img = intent.getStringExtra("img")!!
        val code = intent.getIntExtra("code", 0)
        val gtime = intent.getIntExtra("gtime", 0)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                val getNotification = launch {
                    timerRepository.timerPreferences.onEach { prefs ->
                        notificationManager?.sendNotification(
                            message = msg,
                            img = img,
                            code = code,
                            applicationContext = applicationContext,
                            gtime = gtime,
                            intervalFirstTimerSetting = prefs.intervalFirstTimerSetting,
                            intervalSecondTimerSetting = prefs.intervalSecondTimerSetting
                        )
                    }.collect()
                }

                val deleteTimer = launch {
                    if(code > 300)
                        timerRepository.deleteTimer(bossName = msg.split("<",">")[0])
                }

                select<Unit> {
                    getNotification.onJoin
                    deleteTimer.onJoin
                    stopSelf()
                }
            }
        }

        return START_NOT_STICKY
    }

    private fun createNotificationChannel(context: Context) {
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("TwomBossAlarm", name, importance).apply {
            description = descriptionText
            enableVibration(true)
            setShowBadge(true)
            enableLights(true)
            lightColor = Color.BLUE
        }

        notificationManager?.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onDestroy() {
        notificationManager = null
        super.onDestroy()
    }
}