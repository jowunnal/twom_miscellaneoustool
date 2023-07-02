package com.jinproject.twomillustratedbook.ui.service

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jinproject.twomillustratedbook.ui.receiver.AlarmReceiver
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.AlarmItem
import com.jinproject.twomillustratedbook.utils.day
import com.jinproject.twomillustratedbook.utils.hour
import com.jinproject.twomillustratedbook.utils.minute
import com.jinproject.twomillustratedbook.utils.second
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class ReAlarmService : LifecycleService() {
    @Inject
    lateinit var repository: com.jinproject.domain.repository.TimerRepository
    private var alarmManager: AlarmManager? = null
    private var notificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        notificationManager =
            applicationContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        lifecycleScope.launch {
            val genTime = intent?.getIntExtra("gtime", 0)!!
            val code = intent.getIntExtra("code", 0)
            val monsterName = intent.getStringExtra("msg")!!.split("<", ">").first()
            val nextGenTime = (genTime * 1000).toLong() + System.currentTimeMillis()
            notificationManager?.cancel(code)
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val cal = Calendar.getInstance().apply {
                    timeInMillis = nextGenTime
                }
                repository.getTimer().zip(repository.getTimerPreferences()) { timerModels, prefs ->

                    val timer = timerModels.find { timerModel ->
                        timerModel.bossName == monsterName
                    }

                    val id = timer?.id
                        ?: if (timerModels.isNotEmpty()) timerModels.maxOf { item -> item.id } + 1 else 1

                    when (timer) {
                        is com.jinproject.domain.model.TimerModel -> {
                            repository.updateTimer(
                                id = id,
                                day = cal.day,
                                hour = cal.hour,
                                min = cal.minute,
                                sec = cal.second
                            )
                        }
                        null -> {
                            repository.setTimer(
                                id = id,
                                day = cal.day,
                                hour = cal.hour,
                                min = cal.minute,
                                sec = cal.second,
                                bossName = monsterName
                            )
                        }
                    }

                    makeAlarm(
                        alarmManager!!,
                        application,
                        (nextGenTime - prefs.intervalFirstTimerSetting * 60000),
                        AlarmItem(
                            monsterName,
                            intent.getStringExtra("img")!!,
                            id,
                            genTime
                        ),
                        intervalFirstTimerSetting = prefs.intervalFirstTimerSetting
                    )

                    makeAlarm(
                        alarmManager!!,
                        application,
                        (nextGenTime - prefs.intervalSecondTimerSetting * 60000),
                        AlarmItem(
                            monsterName,
                            intent.getStringExtra("img")!!,
                            id + 300,
                            genTime
                        ),
                        intervalSecondTimerSetting = prefs.intervalSecondTimerSetting
                    )
                }.collect()
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        // We don't provide binding, so return null
        return null
    }

    private fun makeAlarm(
        alarmManager: AlarmManager,
        app: Application,
        nextGenTime: Long,
        item: AlarmItem,
        intervalFirstTimerSetting: Int = 0,
        intervalSecondTimerSetting: Int = 0
    ) {
        val notifyIntentImmediately = Intent(app, AlarmReceiver::class.java)
        notifyIntentImmediately.putExtra("msg", item.name)
        notifyIntentImmediately.putExtra("img", item.imgName)
        notifyIntentImmediately.putExtra("code", item.code)
        notifyIntentImmediately.putExtra("gtime", item.gtime)
        notifyIntentImmediately.putExtra("first", intervalFirstTimerSetting)
        notifyIntentImmediately.putExtra("second", intervalSecondTimerSetting)

        val notifyPendingIntent = PendingIntent.getBroadcast(
            app,
            item.code,
            notifyIntentImmediately,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(
                nextGenTime,
                notifyPendingIntent
            ), notifyPendingIntent
        )
    }

    override fun onDestroy() {
        notificationManager = null
        alarmManager = null
        super.onDestroy()
    }
}