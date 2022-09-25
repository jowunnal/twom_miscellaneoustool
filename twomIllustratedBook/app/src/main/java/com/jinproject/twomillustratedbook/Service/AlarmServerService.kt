package com.jinproject.twomillustratedbook.Service

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.*
import com.jinproject.twomillustratedbook.Database.BookDatabase
import com.jinproject.twomillustratedbook.Database.Entity.DropListMonster
import com.jinproject.twomillustratedbook.Item.AlarmItem
import com.jinproject.twomillustratedbook.Item.Room
import com.jinproject.twomillustratedbook.Receiver.AlarmReceiver
import com.jinproject.twomillustratedbook.Repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


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

                try{for(value in data?.roomBossList!!){
                    var count=value.hour*3600+value.min*60+value.sec
                    val getTime= SimpleDateFormat("HH:mm:ss").format(Date(System.currentTimeMillis())) // simpledateformat의 hh는0-11, HH는0-23 이다.
                    val nowTime=getTime.split(":")
                    count-=(java.lang.Integer.parseInt(nowTime[0])*3600+ java.lang.Integer.parseInt(nowTime[1])*60+Integer.parseInt(nowTime[2]))
                    if(count>0){
                        lifecycleScope.launch(Dispatchers.IO){
                            monster= withContext(Dispatchers.IO) { repository.getMonsInfo(value.name) }
                            val timerSharedPreferences=applicationContext.getSharedPreferences("TimerSharedPref",Context.MODE_PRIVATE)
                            val first=timerSharedPreferences.getInt("first",5)
                            val last=timerSharedPreferences.getInt("last",0)
                            makeAlarm(alarmManager,application,(count-first*60)*1000,AlarmItem(
                                monster.mons_name,
                                monster.mons_imgName,
                                monster.mons_Id,
                                monster.mons_gtime))

                            makeAlarm(alarmManager,application,(count-last*60)*1000,AlarmItem(
                                monster.mons_name,
                                monster.mons_imgName,
                                monster.mons_Id+300,
                                monster.mons_gtime))
                            repository.setTimer(value.day,value.hour,value.min,value.sec,value.name,1)
                        }
                    }
                }}catch (e:NullPointerException){
                    Toast.makeText(applicationContext,"서버상에 등록된 보스목록이 없습니다.",Toast.LENGTH_SHORT).show()
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
        applicationContext.getSharedPreferences("login",Context.MODE_PRIVATE).edit().putBoolean("statue",false).apply()
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