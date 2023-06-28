package com.miscellaneoustool.app.data.datasource.cache.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.miscellaneoustool.app.data.datasource.cache.database.entity.Timer
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {

    @Query("insert into Timer(timerId,day,hour,min,sec,ota,timerMonsName) values (:id,:day,:hour,:min,:sec,0,:name)")
    suspend fun setTimer(id: Int, day: Int, hour: Int, min: Int, sec: Int, name: String)

    @Query("select * from Timer order by Timer.day, Timer.hour, Timer.min, Timer.sec")
    fun getTimer(): Flow<List<Timer>>

    @Query("update Timer set day = :day, hour = :hour, min = :min, sec = :sec where timerId like :id")
    suspend fun updateTimer(id: Int, day: Int, hour: Int, min: Int, sec: Int)

    @Query("delete from Timer where Timer.timerMonsName like :name")
    suspend fun deleteTimer(name: String)

    @Query("update Timer set ota=:ota where timerMonsName like :name")
    suspend fun setOta(ota: Int, name: String)
}