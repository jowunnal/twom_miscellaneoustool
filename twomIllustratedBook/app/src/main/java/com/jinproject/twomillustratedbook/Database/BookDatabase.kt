package com.jinproject.twomillustratedbook.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jinproject.twomillustratedbook.Database.Dao.CollectionDao
import com.jinproject.twomillustratedbook.Database.Dao.DropListDao
import com.jinproject.twomillustratedbook.Database.Dao.LoginDao
import com.jinproject.twomillustratedbook.Database.Dao.TimerDao
import com.jinproject.twomillustratedbook.Database.Entity.*
import com.jinproject.twomillustratedbook.Database.Entity.Map
import com.jinproject.twomillustratedbook.Item.User

@Database(entities = [Book::class, Item::class,Monster::class,Map::class,MonsDropItem::class, MonsLiveAtMap::class,RegisterItemToBook::class, Timer::class, User::class],
    version = 6,  exportSchema = true)
abstract class BookDatabase : RoomDatabase() {
    abstract fun getDropListDao() : DropListDao
    abstract fun getCollectionDao() : CollectionDao
    abstract fun getLoginDao() : LoginDao
    abstract  fun getTimerDao() : TimerDao
}