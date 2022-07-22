package com.jinproject.twomillustratedbook.Service

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.jinproject.twomillustratedbook.Database.BookDatabase
import com.jinproject.twomillustratedbook.Item.AlarmItem
import com.jinproject.twomillustratedbook.Receiver.AlarmReceiver
import com.jinproject.twomillustratedbook.Repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ReAlarmService : LifecycleService() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val alarmManager: AlarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val gtime=intent?.getIntExtra("gtime",0)!!

        makeAlarm(alarmManager,application,10*1000,AlarmItem(
            intent.getStringExtra("msg")!!,
            intent.getStringExtra("img")!!,
           intent.getIntExtra("code", 0)-300,
           gtime))

        makeAlarm(alarmManager,application,20*1000,AlarmItem(
            intent.getStringExtra("msg")!!,
            intent.getStringExtra("img")!!,
            intent.getIntExtra("code",0),
            gtime))


        val cal = Calendar.getInstance()
        var day=cal.get(Calendar.DAY_OF_WEEK)
        var hour=cal.get(Calendar.HOUR_OF_DAY)+((gtime/60)/60)
        var min=cal.get(Calendar.MINUTE)+((gtime/60)%60)
        var sec=cal.get(Calendar.SECOND)+((gtime%60))
        while(sec>=60){
            min+=1
            sec-=60
        }
        while(min>=60){
            hour+=1
            min-=60
        }
        while(hour>=24){
            hour-=24
            day+=1
        }
        val repository= BookRepository(BookDatabase.getInstance(applicationContext).bookDao())
        lifecycleScope.launch(Dispatchers.IO){repository.setTimer(day,hour,min,sec,intent.getStringExtra("msg")!!,1)}
        return START_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        // We don't provide binding, so return null
        return null
    }
    private fun makeAlarm(alarmManager:AlarmManager,app:Application,count:Int,item: AlarmItem){
        val notifyIntentImmediately = Intent(app, AlarmReceiver::class.java)
        notifyIntentImmediately.putExtra("msg",item.name)
        notifyIntentImmediately.putExtra("img",item.imgName)
        notifyIntentImmediately.putExtra("code",item.code)
        notifyIntentImmediately.putExtra("gtime",item.gtime)
        val notifyPendingIntent = PendingIntent.getBroadcast(app,item.code,notifyIntentImmediately,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(System.currentTimeMillis()+count,notifyPendingIntent), notifyPendingIntent)
    }
}