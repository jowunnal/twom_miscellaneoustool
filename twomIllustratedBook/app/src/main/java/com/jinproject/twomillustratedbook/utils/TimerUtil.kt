package com.jinproject.twomillustratedbook.utils

import com.jinproject.twomillustratedbook.data.database.Entity.Monster
import com.jinproject.twomillustratedbook.data.database.Entity.Timer
import com.jinproject.twomillustratedbook.domain.Item.TimerItem
import java.util.*

fun calculateTimer(cal: Calendar,monster: Monster): TimerItem { // 일,시,분,초의 넘어가는 일,시,분,초의 값을 계산
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

fun sortTimerList(timerList: ArrayList<Timer>){
    Collections.sort(timerList, object : Comparator<Timer> { // 일-시-분-초 순으로 정렬
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

fun getMonsterCode(monsName:String):Int{ // 타이머설정을 위한 unique 한 코드값 생성
    var monsCode=0
    for(i in monsName.toCharArray()){
        monsCode+=i.toInt()
    }
    return monsCode
}