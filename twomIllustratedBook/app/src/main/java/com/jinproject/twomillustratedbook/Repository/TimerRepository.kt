package com.jinproject.twomillustratedbook.Repository

import androidx.lifecycle.LiveData
import com.jinproject.twomillustratedbook.Database.Entity.Timer

interface TimerRepository {

    suspend fun setTimer(day:Int,hour:Int, min:Int,sec:Int, name:String)
    fun getTimer() : LiveData<List<Timer>>
    suspend fun setOta(ota:Int,name:String)

}