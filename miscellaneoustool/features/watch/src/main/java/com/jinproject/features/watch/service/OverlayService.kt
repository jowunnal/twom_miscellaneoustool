package com.jinproject.features.watch.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.domain.repository.TimerRepository
import com.jinproject.domain.usecase.timer.GetOverlaySettingUsecase
import com.jinproject.features.alarm.mapper.toTimerState
import com.jinproject.features.watch.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class OverlayService : LifecycleService() {
    private var mView: View? = null

    @Inject
    lateinit var timerRepository: TimerRepository

    @Inject
    lateinit var getOverlaySettingUsecase: GetOverlaySettingUsecase

    override fun onCreate() {
        super.onCreate()

        val exitIntent = Intent(this, OverlayService::class.java).apply {
            putExtra("status", true)
        }
        val exitPendingIntent = PendingIntent.getService(
            this,
            999,
            exitIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification =
            NotificationCompat.Builder(applicationContext, "WatchNotificationChannel")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(R.mipmap.ic_main)
                .setContentTitle(getString(R.string.service_overlay_running))
                .addAction(
                    R.drawable.img_delete_alarm,
                    getString(R.string.turnoff),
                    exitPendingIntent
                )
                .build()

        startForeground(999, notification)

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        mView = inflater.inflate(R.layout.alarm_tv_onotherapps, null)

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

        getOverlaySettingUsecase()
            .flowOn(Dispatchers.IO)
            .onEach { overlaySetting ->
                mView?.findViewById<TextView>(R.id.tv_onOtherApps)?.textSize =
                    overlaySetting.fontSize.toFloat()
                mView?.findViewById<TextView>(R.id.tv_currentTimes)?.textSize =
                    overlaySetting.fontSize.toFloat()

                wm.updateViewLayout(
                    mView,
                    params.apply {
                        x = overlaySetting.xPos
                        y = overlaySetting.yPos
                    }
                )

            }.flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)

        timerRepository.getTimer().map { timerModels ->
            timerModels.filter { timerModel ->
                timerModel.isOverlayOnOrNot
            }.map { timerModel -> timerModel.toTimerState() }
        }.flowOn(Dispatchers.IO)
            .onEach { timerStates ->
                mView?.findViewById<TextView>(R.id.tv_onOtherApps)?.text =
                    timerStates.joinToString("\n") { timerState ->
                        val hourOfDay = this@OverlayService.doOnLocaleLanguage(
                            onKo = timerState.timeState.day.displayOnKo,
                            onElse = timerState.timeState.day.displayOnElse
                        )
                        "${timerState.bossName} (${hourOfDay}) ${timerState.timeState.getMeridiem()} ${timerState.timeState.getTime12Hour()}:${timerState.timeState.minutes}:${timerState.timeState.seconds}"
                    }
            }.flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)
    }

    override fun onBind(p0: Intent): IBinder? {
        super.onBind(p0)
        return null
    }

    @SuppressLint("SimpleDateFormat", "CutPasteId")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent?.getBooleanExtra("status", false) == true)
            stopSelf()

        lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                mView?.findViewById<TextView>(R.id.tv_currentTimes)?.text =
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
}