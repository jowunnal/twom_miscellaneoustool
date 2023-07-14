package com.jinproject.twomillustratedbook.ui.service

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.jinproject.domain.usecase.alarm.SetAlarmUsecase
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.receiver.AlarmReceiver
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.AlarmItem
import com.jinproject.twomillustratedbook.utils.hour
import com.jinproject.twomillustratedbook.utils.minute
import com.jinproject.twomillustratedbook.utils.second
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class ReAlarmService : LifecycleService() {
    @Inject lateinit var setAlarmUsecase: SetAlarmUsecase
    private var notificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val code = intent?.getIntExtra("code", 0)!!
        notificationManager?.cancel(code)

        val monsterName = intent.getStringExtra("name")!!
        val cal = Calendar.getInstance()

        setAlarmUsecase.invoke(
            monsterName = monsterName,
            monsDiedHour = cal.hour,
            monsDiedMin = cal.minute,
            monsDiedSec = cal.second,
            makeAlarm = { intervalFirst, intervalSecond, monsterAlarmModel ->
                makeAlarm(
                    nextGenTime = (monsterAlarmModel.nextGtime - intervalFirst * 60000),
                    item = AlarmItem(
                        monsterAlarmModel.name,
                        monsterAlarmModel.img,
                        monsterAlarmModel.code
                    ),
                    intervalFirstTimerSetting = intervalFirst
                )

                makeAlarm(
                    nextGenTime = (monsterAlarmModel.nextGtime - intervalSecond * 60000),
                    item = AlarmItem(
                        monsterAlarmModel.name,
                        monsterAlarmModel.img,
                        monsterAlarmModel.code + 300
                    ),
                    intervalSecondTimerSetting = intervalSecond
                )
                Toast.makeText(this,"$monsterName ${getString(R.string.alarm_setted)}",Toast.LENGTH_LONG).show()
                stopSelf()
            }
        ).launchIn(lifecycleScope)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private fun makeAlarm(
        nextGenTime: Long,
        item: AlarmItem,
        intervalFirstTimerSetting: Int = 0,
        intervalSecondTimerSetting: Int = 0
    ) {
        val notifyIntentImmediately = Intent(this, AlarmReceiver::class.java)
        notifyIntentImmediately.putExtra("name", item.name)
        notifyIntentImmediately.putExtra("img", item.imgName)
        notifyIntentImmediately.putExtra("code", item.code)
        notifyIntentImmediately.putExtra("first", intervalFirstTimerSetting)
        notifyIntentImmediately.putExtra("second", intervalSecondTimerSetting)

        val notifyPendingIntent = PendingIntent.getBroadcast(
            this,
            item.code,
            notifyIntentImmediately,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(
                nextGenTime,
                notifyPendingIntent
            ), notifyPendingIntent
        )
    }

    override fun onDestroy() {
        notificationManager = null
        super.onDestroy()
    }
}