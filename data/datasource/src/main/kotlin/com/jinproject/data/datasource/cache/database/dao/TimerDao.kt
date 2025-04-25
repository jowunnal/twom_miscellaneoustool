package com.jinproject.data.datasource.cache.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.jinproject.data.datasource.cache.database.entity.Timer
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {

    @Query("insert into Timer(timerId,epochMilli,ota,timerMonsName) values (:id,:epochMilli,:isOverlaying,:name)")
    suspend fun setTimer(
        id: Int,
        epochMilli: Long,
        name: String,
        isOverlaying: Boolean
    )

    @Query("select * from Timer order by epochMilli")
    fun getTimerList(): Flow<List<Timer>>

    @Query("select * from Timer where Timer.timerMonsName like :name")
    fun getTimer(name: String): Flow<Timer?>

    @Query("update Timer set epochMilli = :epochMilli, ota = :isOverlaying where timerMonsName like :monsName")
    suspend fun updateTimer(
        monsName: String,
        epochMilli: Long,
        isOverlaying: Boolean
    )

    @Query("delete from Timer where Timer.timerMonsName like :name")
    suspend fun deleteTimer(name: String)

    @Query("update Timer set ota = 1 where timerMonsName like :name")
    suspend fun setMonsterOverlaid(name: String)
}