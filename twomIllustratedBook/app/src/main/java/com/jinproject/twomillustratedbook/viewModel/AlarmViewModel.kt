package com.jinproject.twomillustratedbook.viewModel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.*
import com.jinproject.twomillustratedbook.Item.AlarmItem
import com.jinproject.twomillustratedbook.Receiver.AlarmReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel(){
    private val alarmManager:AlarmManager = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val app=context.applicationContext

    fun setAlarm(h:Int,m:Int,item: AlarmItem){
        var count=(h*3600+m*60)+item.gtime
        val getTime=SimpleDateFormat("HH:mm:ss").format(Date(System.currentTimeMillis())) // simpledateformat의 hh는0-11, HH는0-23 이다.
        val nowTime=getTime.split(":")
        count-=(Integer.parseInt(nowTime[0])*3600+Integer.parseInt(nowTime[1])*60+Integer.parseInt(nowTime[2]))

        val timerSharedPreferences=app.getSharedPreferences("TimerSharedPref",Context.MODE_PRIVATE)
        val first=timerSharedPreferences.getInt("first",5)
        val last=timerSharedPreferences.getInt("last",0)
        makeAlarm((count-first*60)*1000, AlarmItem(item.name,item.imgName,item.code,item.gtime))
        makeAlarm((count-last*60)*1000, AlarmItem(item.name,item.imgName,item.code+300,item.gtime))

        Toast.makeText(app,"알람설정완료",Toast.LENGTH_LONG).show()
    }

    fun clearAlarm(code:Int){
        val notifyIntent = Intent(app, AlarmReceiver::class.java)
        val notifyPendingIntent=PendingIntent.getBroadcast(app,code,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(notifyPendingIntent)
    }

    private fun makeAlarm(count:Int,item: AlarmItem){
        val notifyIntentImmediately = Intent(app, AlarmReceiver::class.java)
        notifyIntentImmediately.putExtra("msg",item.name)
        notifyIntentImmediately.putExtra("img",item.imgName)
        notifyIntentImmediately.putExtra("code",item.code)
        notifyIntentImmediately.putExtra("gtime",item.gtime)
        val notifyPendingIntent = PendingIntent.getBroadcast(app,item.code,notifyIntentImmediately,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(System.currentTimeMillis()+count,notifyPendingIntent), notifyPendingIntent)
    }


}
