package com.jinproject.features.alarm.alarm.service

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.jinproject.core.util.toEpochMilli
import com.jinproject.design_ui.R
import com.jinproject.domain.repository.DropListRepository
import com.jinproject.domain.usecase.alarm.ManageTimerSettingUsecase
import com.jinproject.domain.usecase.alarm.SetAlarmUsecase
import com.jinproject.features.alarm.alarm.utils.AlarmItem
import com.jinproject.features.alarm.alarm.utils.makeAlarm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import javax.inject.Inject

@AndroidEntryPoint
class ReAlarmService : LifecycleService() {
    @Inject
    lateinit var setAlarmUsecase: SetAlarmUsecase

    @Inject
    lateinit var manageTimerSettingUsecase: ManageTimerSettingUsecase

    @Inject
    lateinit var dropListRepository: DropListRepository

    private val notificationManager: NotificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }
    private val alarmManager by lazy { getSystemService(ALARM_SERVICE) as AlarmManager }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val code = intent?.getIntExtra("code", 0)!!
        notificationManager.cancel(code)
        notificationManager.cancel(code - 300)

        val monsterName = intent.getStringExtra("name")!!
        val dateTime = ZonedDateTime.now()

        lifecycleScope.launch(Dispatchers.Main.immediate) {
            val timer = withContext(Dispatchers.IO) {
                setAlarmUsecase(
                    monsterName = monsterName,
                    deadTime = dateTime
                )
            }

            val intervals = withContext(Dispatchers.IO) {
                manageTimerSettingUsecase.getTimerSetting().first().interval
            }

            val monsterImage = withContext(Dispatchers.IO) {
                dropListRepository.getMonster(monsterName).first().imageName
            }

            with(timer[0]) {
                alarmManager.makeAlarm(
                    context = this@ReAlarmService,
                    nextGenTime = this.dateTime.toEpochMilli(),
                    item = AlarmItem(
                        name = monsterName,
                        imgName = monsterImage,
                        code = id
                    ),
                    intervalFirstTimerSetting = intervals?.firstInterval ?: 0,
                )
            }

            with(timer[1]) {
                alarmManager.makeAlarm(
                    context = this@ReAlarmService,
                    nextGenTime = this.dateTime.toEpochMilli(),
                    item = AlarmItem(
                        name = monsterName,
                        imgName = monsterImage,
                        code = id + 300
                    ),
                    intervalSecondTimerSetting = intervals?.secondInterval ?: 0,
                )
            }

            Toast.makeText(
                this@ReAlarmService,
                "$monsterName ${getString(R.string.alarm_setted)}",
                Toast.LENGTH_LONG
            ).show()
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }
}