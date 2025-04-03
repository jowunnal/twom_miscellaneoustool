package com.jinproject.data.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jinproject.data.datasource.cache.database.dao.CollectionDao
import com.jinproject.data.datasource.cache.database.dao.DropListDao
import com.jinproject.data.datasource.cache.database.dao.SimulatorDao
import com.jinproject.data.datasource.cache.database.dao.TimerDao
import com.jinproject.data.datasource.cache.database.entity.Book
import com.jinproject.data.datasource.cache.database.entity.Equipment
import com.jinproject.data.datasource.cache.database.entity.Item
import com.jinproject.data.datasource.cache.database.entity.ItemInfo
import com.jinproject.data.datasource.cache.database.entity.Maps
import com.jinproject.data.datasource.cache.database.entity.MonsDropItem
import com.jinproject.data.datasource.cache.database.entity.MonsLiveAtMap
import com.jinproject.data.datasource.cache.database.entity.Monster
import com.jinproject.data.datasource.cache.database.entity.RegisterItemToBook
import com.jinproject.data.datasource.cache.database.entity.Stat
import com.jinproject.data.datasource.cache.database.entity.Timer

@Database(
    entities = [Book::class, Item::class, ItemInfo::class, Equipment::class, Monster::class, Maps::class, Stat::class, MonsDropItem::class, MonsLiveAtMap::class, RegisterItemToBook::class, Timer::class],
    version = 6,
    exportSchema = true,
)
abstract class BookDatabase : RoomDatabase() {

    abstract fun getDropListDao(): DropListDao
    abstract fun getCollectionDao(): CollectionDao
    abstract fun getTimerDao(): TimerDao
    abstract fun getSimulatorDao(): SimulatorDao
}