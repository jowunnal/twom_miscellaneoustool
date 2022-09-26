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

    companion object{
        @Volatile
        private var bookInstance : BookDatabase ?= null

        fun getInstance(context: Context) : BookDatabase {
            return bookInstance ?: synchronized(BookDatabase::class){ //syncronized()는 클래스파일에 멀티스레드가 접근하지못하도록 하기위해 사용
                val instance = Room.databaseBuilder(context.applicationContext,
                BookDatabase::class.java, "book_Database").createFromAsset("database/db_twom_2.db")
                    .addMigrations().fallbackToDestructiveMigration().build()
                bookInstance=instance
                instance
            }
        }
    }
}