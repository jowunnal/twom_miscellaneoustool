package com.jinproject.twomillustratedbook.Item

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.*
import java.text.SimpleDateFormat
import java.util.*

class AlarmModel(application: Application) : AndroidViewModel(application){
    private val alarmManager:AlarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val app=application

    fun setAlarm(h:Int,m:Int,item: AlarmItem){
        var count=(h*3600+m*60)+item.gtime
        val getTime=SimpleDateFormat("HH:mm").format(Date(System.currentTimeMillis())) // simpledateformat의 hh는0-11, HH는0-23 이다.
        val nowTime=getTime.split(":")
        count-=(Integer.parseInt(nowTime[0])*3600+Integer.parseInt(nowTime[1])*60)

        makeAlarm((count-300)*1000,AlarmItem(item.name,item.imgName,item.code,item.gtime))
        makeAlarm(count*1000,AlarmItem(item.name,item.imgName,item.code+300,item.gtime))
        Toast.makeText(getApplication(),"알람설정완료",Toast.LENGTH_LONG).show()
    }

    fun clearAlarm(code:Int){
        val notifyIntent = Intent(app,AlarmReceiver::class.java)
        val notifyPendingIntent=PendingIntent.getBroadcast(app,code,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(notifyPendingIntent)
    }

    private fun makeAlarm(count:Int,item:AlarmItem){
        val notifyIntentImmediately = Intent(app,AlarmReceiver::class.java)
        notifyIntentImmediately.putExtra("msg",item.name)
        notifyIntentImmediately.putExtra("img",item.imgName)
        notifyIntentImmediately.putExtra("code",item.code)
        val notifyPendingIntent = PendingIntent.getBroadcast(app,item.code,notifyIntentImmediately,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(System.currentTimeMillis()+count,notifyPendingIntent), notifyPendingIntent)
    }
}
