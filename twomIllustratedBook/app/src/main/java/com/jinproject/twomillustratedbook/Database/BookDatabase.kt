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

@Database(entities = [BookEntity::class, BookContentEntity::class,DropListMonster::class,DropListItems::class, DropListMaps::class, Timer::class],
    version = 4,  exportSchema = true)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao() : BookDao

    companion object{
        @Volatile
        private var bookInstance : BookDatabase ?=null
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("update DropListMonster set mons_type='normal' where mons_name = '마녀딜린' or mons_name='우크파나' or mons_name='바슬라프' or mons_name='세피아' or mons_name='일루스트'")
                database.execSQL("update DropListMonster set mons_gtime=3920 where mons_name='파웃보'")
                database.execSQL("update DropListMonster set mons_gtime=4010 where mons_name='빨웃보'")
                database.execSQL("update DropListMonster set mons_gtime=4110 where mons_name='초웃보'")
            }
        }
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table Timer add timer_day INTEGER not null default 0")
                database.execSQL("update Timer set timer_day=0")
                database.execSQL("alter table Timer add timer_ota INTEGER not null default 0")
                database.execSQL("update Timer set timer_ota=0")
                database.execSQL("update DropListMonster set mons_type='bigboss' where mons_name = '마녀딜린' or mons_name='우크파나' or mons_name='바슬라프' or mons_name='일루스트' or mons_name='빅마마'")
                database.execSQL("update DropListMonster set mons_gtime=75360 where mons_name='빅마마'")
                database.execSQL("update DropListMonster set mons_gtime=75360 where mons_name='우크파나'")
                database.execSQL("update DropListMonster set mons_gtime=79800 where mons_name='바슬라프'")
                database.execSQL("update DropListMonster set mons_gtime=172800 where mons_name='마녀딜린'")
                database.execSQL("update DropListMonster set mons_gtime=172800 where mons_name='일루스트'")
            }
        }

        fun getInstance(context: Context) : BookDatabase {
            return bookInstance ?: synchronized(BookDatabase::class){
                val instance = Room.databaseBuilder(context.applicationContext,
                BookDatabase::class.java, "book_Database").createFromAsset("database/db_twom.db")
                    .addMigrations(MIGRATION_2_3,MIGRATION_3_4).build()
                bookInstance=instance
                instance
            }
        }
    }
}