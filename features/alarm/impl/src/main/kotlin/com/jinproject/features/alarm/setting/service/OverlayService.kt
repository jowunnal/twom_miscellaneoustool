package com.jinproject.features.alarm.setting.service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.jinproject.design_ui.R
import com.jinproject.domain.repository.TimerRepository
import com.jinproject.domain.usecase.alarm.ManageTimerSettingUsecase
import com.jinproject.features.alarm.alarm.item.TimerState
import com.jinproject.features.alarm.alarm.utils.createChannel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class OverlayService : LifecycleService() {
    private var mView: View? = null
    private val wm by lazy { getSystemService(WINDOW_SERVICE) as WindowManager }
    private lateinit var notification: NotificationCompat.Builder

    @Inject
    lateinit var timerRepository: TimerRepository

    @Inject
    lateinit var manageTimerSettingUsecase: ManageTimerSettingUsecase

    @SuppressLint("WrongConstant")
    override fun onCreate() {
        super.onCreate()

        val exitIntent = Intent(this, OverlayService::class.java).apply {
            putExtra("status", true)
        }
        val exitPendingIntent = PendingIntent.getService(
            this,
            NOTIFICATION_ID,
            exitIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createChannel(this)

        notification =
            NotificationCompat.Builder(applicationContext, "TwomBossAlarm")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(R.mipmap.ic_main)
                .setContentTitle(getString(R.string.service_overlay_running))
                .addAction(
                    R.drawable.img_delete_alarm,
                    getString(R.string.turnoff),
                    exitPendingIntent
                )

        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = inflater.inflate(com.jinproject.features.alarm.R.layout.alarm_tv_onotherapps, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        }
        wm.addView(mView, params)

        manageTimerSettingUsecase.getTimerSetting()
            .flowOn(Dispatchers.IO)
            .onEach { overlaySetting ->
                mView?.findViewById<TextView>(com.jinproject.features.alarm.R.id.tv_onOtherApps)?.textSize =
                    overlaySetting.fontSize?.toFloat() ?: 0f
                mView?.findViewById<TextView>(com.jinproject.features.alarm.R.id.tv_currentTimes)?.textSize =
                    overlaySetting.fontSize?.toFloat() ?: 0f

                if (mView?.windowToken != null && mView?.parent != null)
                    mView?.let {
                        wm.updateViewLayout(
                            mView,
                            params.apply {
                                x = overlaySetting.xPos ?: 0
                                y = overlaySetting.yPos ?: 0
                            }
                        )
                    }
            }.flowOn(Dispatchers.Main.immediate)
            .launchIn(lifecycleScope)

        timerRepository.getTimerList(true).map { timerModels ->
            timerModels.map { timerModel -> TimerState.fromDomain(timerModel) }
        }.flowOn(Dispatchers.IO)
            .onEach { timerStates ->
                mView?.findViewById<TextView>(com.jinproject.features.alarm.R.id.tv_onOtherApps)?.text =
                    timerStates.joinToString("\n") { timerState ->
                        "${timerState.bossName} ${
                            timerState.dateTime.format(
                                DateTimeFormatter.ofPattern(
                                    "MM.dd (E) a hh:mm:ss"
                                )
                            )
                        }"
                    }
            }.flowOn(Dispatchers.Main.immediate)
            .launchIn(lifecycleScope)
    }

    override fun onBind(p0: Intent): IBinder? {
        super.onBind(p0)
        return null
    }

    @SuppressLint("SimpleDateFormat", "CutPasteId")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent != null && ::notification.isInitialized)
            notification.build().also {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                    startForeground(
                        NOTIFICATION_ID, it,
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                    )
                else
                    startForeground(NOTIFICATION_ID, it)
            }

        if (intent?.getBooleanExtra("status", false) == true) {
            wm.removeView(mView)
            stopSelf()
        }

        lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                mView?.findViewById<TextView>(com.jinproject.features.alarm.R.id.tv_currentTimes)?.text =
                    SimpleDateFormat("a hh:mm:ss").format(Date(System.currentTimeMillis()))
                delay(1000)
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        if (mView != null) {
            mView = null
        }
        super.onDestroy()
    }

    companion object {
        const val NOTIFICATION_ID = 999
    }
}