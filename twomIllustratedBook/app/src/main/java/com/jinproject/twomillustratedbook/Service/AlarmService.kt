package com.jinproject.twomillustratedbook.Service
import android.content.Intent
import android.os.*
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.jinproject.twomillustratedbook.Database.BookDatabase
import com.jinproject.twomillustratedbook.Repository.BookRepositoryImpl
import com.jinproject.twomillustratedbook.Repository.BookRepositoryModule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService : LifecycleService() {
    @Inject lateinit var repository:BookRepositoryImpl
    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
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