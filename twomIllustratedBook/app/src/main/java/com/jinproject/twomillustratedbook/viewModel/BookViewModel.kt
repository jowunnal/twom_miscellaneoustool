package com.jinproject.twomillustratedbook.Item

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.jinproject.twomillustratedbook.Database.Entity.Monster
import com.jinproject.twomillustratedbook.Database.Entity.Timer
import com.jinproject.twomillustratedbook.Repository.BookRepository
import com.jinproject.twomillustratedbook.Repository.BookRepositoryImpl
import com.jinproject.twomillustratedbook.Repository.BookRepositoryModule
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository: BookRepositoryImpl,@ApplicationContext private val context:Context) : ViewModel() {
    var data_map=""
    var dataItemType=""
    var alarmItem = AlarmItem()
    private var clickable = -1
    lateinit var monster:Monster
    fun content(data:String) = repository.bookList(data)
    val droplistMaps:LiveData<List<String>> = repository.maps
    fun inputData(data:String) = repository.inputdata(data)
    fun getNameSp(inputData:String) = repository.getNameSp(inputData)
    fun setTimer(timerItem: TimerItem)=viewModelScope.launch(Dispatchers.IO){repository.setTimer(timerItem.day,timerItem.hour,timerItem.min,timerItem.sec,timerItem.name)}
    val timer:LiveData<List<Timer>> = repository.timer
    fun setOta(ota:Int,name:String) = viewModelScope.launch(Dispatchers.IO) {  repository.setOta(ota,name)}
    suspend fun getMonsInfo(inputData: String):Monster{
        val info=viewModelScope.async(Dispatchers.IO){repository.getMonsInfo(inputData) }
        return info.await()
    }
    /*fun getBossSp()=repository.getBossSp()
    fun getBigBossSp()=repository.getBigBossSp()*/

    fun checkIsClickedBoss(pos:Int,inputData: String){
        when (clickable) {
            -1 -> {
                clickable=pos
                CoroutineScope(Dispatchers.IO).launch {
                    monster=getMonsInfo(inputData)
                    alarmItem=AlarmItem(monster.monsName,monster.monsImgName,monster.monsName.toInt(),monster.monsGtime)
                }
            }
            pos -> {
                clickable=-1
            }
            else -> {
                Toast.makeText(context.applicationContext,"동시에 2개이상을 선택할수 없습니다. 해제후 다시선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun calculateTimer(cal: Calendar): TimerItem { // 일,시,분,초의 넘어가는 일,시,분,초의 값을 계산
        var day = cal.get(Calendar.DAY_OF_WEEK)
        var hour = cal.get(Calendar.HOUR_OF_DAY) + ((monster.monsGtime / 60) / 60)
        var min = cal.get(Calendar.MINUTE) + ((monster.monsGtime / 60) % 60)
        var sec = cal.get(Calendar.SECOND) + ((monster.monsGtime % 60))
        while (sec >= 60) {
            min += 1
            sec -= 60
        }
        while (min >= 60) {
            hour += 1
            min -= 60
        }
        while (hour >= 24) {
            hour -= 24
            day += 1
        }
        return TimerItem(monster.monsName,day,hour,min,sec)
    }

    fun sortTimerList(timerList:ArrayList<Timer>){
        Collections.sort(timerList, object :Comparator<Timer>{ // 일-시-분-초 순으로 정렬
            override fun compare(p0: Timer?, p1: Timer?): Int {
                if(p0!!.day.compareTo(p1!!.day)==0){
                    if(p0.hour.compareTo(p1.hour)==0){
                        if(p0.min.compareTo(p1.min)==0){
                            if(p0.sec.compareTo(p1.sec)==0){
                                return p0.timerMonsName.compareTo(p1.timerMonsName)
                            }
                            return p0.sec.compareTo(p1.sec)
                        }
                        return p0.min.compareTo(p1.min)
                    }
                    return p0.hour.compareTo(p1.hour)
                }
                return p0.day.compareTo(p1.day)
            }
        })
    }
}


