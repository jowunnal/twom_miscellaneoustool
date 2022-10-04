package com.jinproject.twomillustratedbook.Database.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.jinproject.twomillustratedbook.Database.Entity.Timer

@Dao
interface TimerDao {
    @Query("update Timer set day= :day,hour= :hour, min= :min,sec=:sec where timerMonsName like:name")
    suspend fun setTimer(day:Int,hour:Int,min:Int,sec:Int,name:String)

    @Query("select * from Timer")
    fun getTimer(): LiveData<List<Timer>>

    @Query("delete from Timer where Timer.timerMonsName like :name")
    suspend fun deleteTimer(name:String)

    @Query("update Timer set ota=:ota where timerMonsName like :name")
    suspend fun setOta(ota:Int,name:String)
}