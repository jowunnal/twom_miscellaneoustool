package com.jinproject.twomillustratedbook.Repository

import com.jinproject.twomillustratedbook.Database.Dao.TimerDao
import javax.inject.Inject

class TimerRepositoryImpl @Inject constructor(private val timerDao: TimerDao):TimerRepository {

    override suspend fun setTimer(day: Int, hour: Int, min: Int, sec: Int, name: String) = timerDao.setTimer(day,hour, min,sec, name)
    override fun deleteTimer(name: String) = timerDao.deleteTimer(name)
    override fun getTimer() = timerDao.getTimer()
    override suspend fun setOta(ota: Int, name: String) = timerDao.setOta(ota,name)

}