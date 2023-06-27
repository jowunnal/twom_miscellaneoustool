package com.miscellaneoustool.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.miscellaneoustool.app.data.database.dao.CollectionDao
import com.miscellaneoustool.app.data.database.dao.DropListDao
import com.miscellaneoustool.app.data.database.dao.TimerDao
import com.miscellaneoustool.app.data.database.entity.Book
import com.miscellaneoustool.app.data.database.entity.Item
import com.miscellaneoustool.app.data.database.entity.Map
import com.miscellaneoustool.app.data.database.entity.MonsDropItem
import com.miscellaneoustool.app.data.database.entity.MonsLiveAtMap
import com.miscellaneoustool.app.data.database.entity.Monster
import com.miscellaneoustool.app.data.database.entity.RegisterItemToBook
import com.miscellaneoustool.app.data.database.entity.Stat
import com.miscellaneoustool.app.data.database.entity.Timer

@Database(entities = [Book::class, Item::class, Monster::class, Map::class, Stat::class, MonsDropItem::class, MonsLiveAtMap::class, RegisterItemToBook::class, Timer::class],
    version = 12,  exportSchema = true)
abstract class BookDatabase : RoomDatabase() {
    abstract fun getDropListDao() : DropListDao
    abstract fun getCollectionDao() : CollectionDao
    abstract  fun getTimerDao() : TimerDao
}