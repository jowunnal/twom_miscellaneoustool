package com.jinproject.features.alarm.service

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.jinproject.core.util.getParcelableExtraOnVersion
import com.jinproject.core.util.hour
import com.jinproject.core.util.minute
import com.jinproject.core.util.second
import com.jinproject.domain.usecase.alarm.SetAlarmUsecase
import com.jinproject.features.alarm.item.AlarmItem
import com.jinproject.features.alarm.utils.makeAlarm
import com.jinproject.features.core.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class ReAlarmService : LifecycleService() {
    @Inject lateinit var setAlarmUsecase: SetAlarmUsecase
    private var notificationManager: NotificationManager? = null
    private val alarmManager by lazy { getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val code = intent?.getIntExtra("code", 0)!!
        notificationManager?.cancel(code)

        val monsterName = intent.getStringExtra("name")!!
        val backToAlarmIntent = intent.getParcelableExtraOnVersion<Intent>("backToAlarmIntent")
        val cal = Calendar.getInstance()

        setAlarmUsecase.invoke(
            monsterName = monsterName,
            monsDiedHour = cal.hour,
            monsDiedMin = cal.minute,
            monsDiedSec = cal.second,
            makeAlarm = { intervalFirst, intervalSecond, monsterAlarmModel ->

                alarmManager.makeAlarm(
                    context = this,
                    nextGenTime = (monsterAlarmModel.nextGtime - intervalFirst * 60000),
                    item = AlarmItem(
                        monsterAlarmModel.name,
                        monsterAlarmModel.img,
                        monsterAlarmModel.code
                    ),
                    intervalFirstTimerSetting = intervalFirst,
                    backToAlarmIntent = backToAlarmIntent
                )

                alarmManager.makeAlarm(
                    context = this,
                    nextGenTime = (monsterAlarmModel.nextGtime - intervalSecond * 60000),
                    item = AlarmItem(
                        monsterAlarmModel.name,
                        monsterAlarmModel.img,
                        monsterAlarmModel.code + 300
                    ),
                    intervalSecondTimerSetting = intervalSecond,
                    backToAlarmIntent = backToAlarmIntent
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

    override fun onDestroy() {
        notificationManager = null
        super.onDestroy()
    }
}