package com.jinproject.twomillustratedbook.Database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jinproject.twomillustratedbook.Database.Dao.BookDao
import com.jinproject.twomillustratedbook.Database.Entity.*
import com.jinproject.twomillustratedbook.Database.Entity.Map

@Database(entities = [Book::class, Item::class,Monster::class,Map::class,MonsDropItem::class, MonsLiveAtMap::class,RegisterItemToBook::class, Timer::class],
    version = 5,  exportSchema = true)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao() : BookDao
}