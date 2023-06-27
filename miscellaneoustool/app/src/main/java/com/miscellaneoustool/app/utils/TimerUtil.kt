package com.miscellaneoustool.app.utils

import com.miscellaneoustool.app.data.database.entity.Timer
import java.util.Collections


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