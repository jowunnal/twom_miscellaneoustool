package com.jinproject.twomillustratedbook.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.jinproject.twomillustratedbook.data.database.Entity.Timer
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {

    @Query("insert into Timer(day,hour,min,sec,ota,timerMonsName) values (:day,:hour,:min,:sec,0,:name)")
    suspend fun setTimer(day:Int,hour:Int,min:Int,sec:Int,name:String)

    @Query("select * from Timer")
    fun getTimer(): Flow<List<Timer>>

    @Query("delete from Timer where Timer.timerMonsName like :name")
    suspend fun deleteTimer(name:String)

    @Query("update Timer set ota=:ota where timerMonsName like :name")
    suspend fun setOta(ota:Int,name:String)
}