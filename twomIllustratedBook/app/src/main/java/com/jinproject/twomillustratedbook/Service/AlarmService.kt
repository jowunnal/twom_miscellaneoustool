package com.jinproject.twomillustratedbook.Service
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.*
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.jinproject.twomillustratedbook.Database.BookDatabase
import com.jinproject.twomillustratedbook.MainActivity.MainActivity
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.Repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlarmService : LifecycleService() {

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val repository=BookRepository(BookDatabase.getInstance(applicationContext).bookDao())
        val name=intent!!.getStringExtra("name")!!
        lifecycleScope.launch(Dispatchers.IO){repository.setTimer(0,0,0,0,name)}
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}