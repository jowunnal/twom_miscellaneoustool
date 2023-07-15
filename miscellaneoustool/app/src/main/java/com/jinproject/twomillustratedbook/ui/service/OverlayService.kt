package com.jinproject.twomillustratedbook.ui.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.allViews
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.io.Files.append
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.domain.repository.TimerRepository
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimerState
import com.jinproject.twomillustratedbook.ui.screen.alarm.mapper.toTimerState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class OverlayService: LifecycleService() {
    private var wm: WindowManager? = null
    private var mView: View? = null
    private var notificationManager: NotificationManager? = null
    @Inject lateinit var timerRepository: TimerRepository

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
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()

        val notification = NotificationCompat.Builder(applicationContext, "WatchNotificationChannel")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.mipmap.ic_main)
            .setContentTitle(getString(R.string.service_overlay_running))
            .addAction(R.drawable.img_delete_alarm, getString(R.string.turnoff), exitPendingIntent)
            .build()

        startForeground(999, notification)

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
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
        wm!!.addView(mView,params)
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("WatchNotificationChannel", name, importance).apply {
            description = descriptionText
            setShowBadge(true)
            enableLights(false)
            lockscreenVisibility = Notification.VISIBILITY_SECRET
            lightColor = Color.BLUE
        }
        notificationManager?.createNotificationChannel(channel)
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

        val list = intent?.getParcelableArrayListExtra<TimerState>("timerList")
        var fontSize = intent?.getIntExtra("fontSize",14)

        lifecycleScope.launch {
            launch(Dispatchers.Main) {
                while (true) {
                    mView?.findViewById<TextView>(R.id.tv_currentTimes)?.text =
                        SimpleDateFormat("a hh:mm:ss").format(Date(System.currentTimeMillis()))
                    delay(1000)
                }
            }
        }

        timerRepository.getTimerPreferences()
            .flowOn(Dispatchers.IO)
            .onEach { timer ->
                mView?.findViewById<TextView>(R.id.tv_onOtherApps)?.textSize = fontSize?.toFloat() ?: timer.fontSize.toFloat()
                mView?.findViewById<TextView>(R.id.tv_currentTimes)?.textSize = fontSize?.toFloat() ?: timer.fontSize.toFloat()
            }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)

        timerRepository.getTimer().map { timerModels ->
            timerModels.filter { timerModel ->
                timerModel.isOverlayOnOrNot
            }.map { timerModel -> timerModel.toTimerState() }
        }.onEach { timerStates ->
            mView?.findViewById<TextView>(R.id.tv_onOtherApps)?.text = kotlin.runCatching { StringBuilder().apply {
                for (item in list!!) {
                    val hourOfDay = this@OverlayService.doOnLocaleLanguage(onKo = item.timeState.day.displayOnKo, onElse = item.timeState.day.displayOnElse)
                    append("${item.bossName} (${hourOfDay}) ${item.timeState.getMeridiem()} ${item.timeState.getTime12Hour()}:${item.timeState.minutes}:${item.timeState.seconds}\n")
                }
            }.toString() }.getOrElse {
                timerStates.joinToString("\n") { timerState ->
                    val hourOfDay = this@OverlayService.doOnLocaleLanguage(onKo = timerState.timeState.day.displayOnKo, onElse = timerState.timeState.day.displayOnElse)
                    "${timerState.bossName} (${hourOfDay}) ${timerState.timeState.getMeridiem()} ${timerState.timeState.getTime12Hour()}:${timerState.timeState.minutes}:${timerState.timeState.seconds}"
                }
            }
        }.launchIn(lifecycleScope)

        val xPos = intent?.getIntExtra("xPos", 0) ?: 0
        val yPos = intent?.getIntExtra("yPos", 0) ?: 0

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            x = xPos
            y = yPos
        }

        wm!!.removeView(mView)
        wm!!.addView(mView, params)

        return START_STICKY
    }

    override fun onDestroy() {
        if (wm != null) {
            if (mView != null) {
                wm!!.removeView(mView); // View 초기화
                wm = null
                mView = null
            }
        }
        notificationManager = null
        super.onDestroy()
    }
}