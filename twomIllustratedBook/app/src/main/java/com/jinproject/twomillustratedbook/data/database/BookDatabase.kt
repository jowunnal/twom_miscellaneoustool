package com.jinproject.twomillustratedbook.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jinproject.twomillustratedbook.data.database.dao.CollectionDao
import com.jinproject.twomillustratedbook.data.database.dao.DropListDao
import com.jinproject.twomillustratedbook.data.database.dao.TimerDao
import com.jinproject.twomillustratedbook.data.Entity.*
import com.jinproject.twomillustratedbook.data.Entity.Map

@Database(entities = [Book::class, Item::class, Monster::class, Map::class, MonsDropItem::class, MonsLiveAtMap::class, RegisterItemToBook::class, Timer::class],
    version = 8,  exportSchema = true)
abstract class BookDatabase : RoomDatabase() {
    abstract fun getDropListDao() : DropListDao
    abstract fun getCollectionDao() : CollectionDao
    abstract  fun getTimerDao() : TimerDao
}