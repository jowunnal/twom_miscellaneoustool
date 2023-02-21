package com.jinproject.twomillustratedbook.ui.Service

import android.annotation.SuppressLint
import android.content.Intent
import android.os.IBinder
import android.content.Context
import android.view.Gravity
import android.graphics.PixelFormat
import android.view.WindowManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.jinproject.twomillustratedbook.data.database.Entity.Timer
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.domain.model.WeekModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class OverlayService : LifecycleService() {
    private var wm: WindowManager? = null
    private var mView: View? = null
    override fun onCreate() {
        super.onCreate()
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // inflater 를 사용하여 layout 을 가져오자
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        // 윈도우매니저 설정

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        }
        // 위치 지정

        mView = inflater.inflate(R.layout.alarm_tv_onotherapps, null)
        wm!!.addView(mView, params)

    }

    override fun onBind(p0: Intent): IBinder? {
        super.onBind(p0)
        return null
    }

    @SuppressLint("SimpleDateFormat")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                mView?.findViewById<TextView>(R.id.tv_currentTimes)?.text =
                    SimpleDateFormat("HH:mm:ss").format(Date(System.currentTimeMillis()))
                delay(1000)
            }
        }

        val list = intent?.getParcelableArrayListExtra<Timer>("list")
        if (list != null) {
            var strOta = ""
            for (item in list) {
                strOta += item.timerMonsName + " (" + WeekModel.findByCode(item.day) + ") " + item.hour + ":" + item.min + ":" + item.sec + "\n"
            }
            mView?.findViewById<TextView>(R.id.tv_onOtherApps)?.text = strOta
        } else {
            mView?.findViewById<TextView>(R.id.tv_onOtherApps)?.text = ""
        }

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
        super.onDestroy()
    }
}