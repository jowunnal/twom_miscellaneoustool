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
import androidx.lifecycle.withCreated
import com.google.firebase.database.*
import com.jinproject.twomillustratedbook.Database.BookDatabase
import com.jinproject.twomillustratedbook.Database.Entity.DropListMonster
import com.jinproject.twomillustratedbook.Item.AlarmItem
import com.jinproject.twomillustratedbook.Item.Room
import com.jinproject.twomillustratedbook.Item.TimerItem
import com.jinproject.twomillustratedbook.Receiver.AlarmReceiver
import com.jinproject.twomillustratedbook.Repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashMap


class AlarmServerService :LifecycleService() {
    lateinit var monster:DropListMonster
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val db = FirebaseDatabase.getInstance().reference
        val loginPreference=applicationContext.getSharedPreferences("login",Context.MODE_PRIVATE)
        db.child("RoomList").child(loginPreference.getString("key","")!!).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val alarmManager: AlarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val repository= BookRepository(BookDatabase.getInstance(applicationContext).bookDao())
                val type= object : GenericTypeIndicator<Room>(){}
                val data=snapshot.getValue(type)
                Log.d("test",data?.roomBossList.toString())
                for(value in data?.roomBossList!!){
                    lifecycleScope.launch(Dispatchers.IO){
                        withContext(Dispatchers.IO){ monster=repository.getMonsInfo(value.name) }
                        makeAlarm(alarmManager,application,10*1000,AlarmItem(
                            monster.mons_name,
                            monster.mons_imgName,
                            monster.mons_Id-300,
                            monster.mons_gtime))

                        makeAlarm(alarmManager,application,20*1000,AlarmItem(
                            monster.mons_name,
                            monster.mons_imgName,
                            monster.mons_Id,
                            monster.mons_gtime))

                        repository.setTimer(value.day,value.hour,value.min,value.sec,monster.mons_name,1)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun makeAlarm(alarmManager: AlarmManager, app: Application, count:Int, item: AlarmItem){
        val notifyIntentImmediately = Intent(app, AlarmReceiver::class.java)
        notifyIntentImmediately.putExtra("msg",item.name)
        notifyIntentImmediately.putExtra("img",item.imgName)
        notifyIntentImmediately.putExtra("code",item.code)
        notifyIntentImmediately.putExtra("gtime",item.gtime)
        val notifyPendingIntent = PendingIntent.getBroadcast(app,item.code,notifyIntentImmediately,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(System.currentTimeMillis()+count,notifyPendingIntent), notifyPendingIntent)
    }
}