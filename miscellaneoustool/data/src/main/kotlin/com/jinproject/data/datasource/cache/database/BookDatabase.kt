package com.jinproject.data.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jinproject.data.datasource.cache.database.dao.CollectionDao
import com.jinproject.data.datasource.cache.database.dao.DropListDao
import com.jinproject.data.datasource.cache.database.dao.TimerDao
import com.jinproject.data.datasource.cache.database.entity.Book
import com.jinproject.data.datasource.cache.database.entity.Item
import com.jinproject.data.datasource.cache.database.entity.Maps
import com.jinproject.data.datasource.cache.database.entity.MonsDropItem
import com.jinproject.data.datasource.cache.database.entity.MonsLiveAtMap
import com.jinproject.data.datasource.cache.database.entity.Monster
import com.jinproject.data.datasource.cache.database.entity.RegisterItemToBook
import com.jinproject.data.datasource.cache.database.entity.Stat
import com.jinproject.data.datasource.cache.database.entity.Timer

@Database(entities = [Book::class, Item::class, Monster::class, Maps::class, Stat::class, MonsDropItem::class, MonsLiveAtMap::class, RegisterItemToBook::class, Timer::class],
    version = 2,  exportSchema = true)
abstract class BookDatabase : RoomDatabase() {

    companion object {
        val MIGRATION_1_2_KOR = object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.apply {
                    execSQL("update Monster set monsType = 'Mini Boss' where monsType = 'named'")
                    execSQL("update Monster set monsType = 'Semi Boss' where monsType = 'boss'")
                    execSQL("update Monster set monsType = 'World Boss' where monsType = 'bigboss'")
                    execSQL("insert into Monster values ('플라타니스타', 52, 0, 'platanista', 'World Boss')")
                    execSQL("insert into MonsLiveAtMap values ('플라타니스타', '모르포시즈 정원')")
                    execSQL("insert into MonsDropItem VALUES ('플라타니스타', '현자주기')")
                    execSQL("insert into MonsDropItem VALUES ('플라타니스타', '주문서S')")
                    execSQL("insert into MonsDropItem VALUES ('플라타니스타', '오래된주문서')")
                    execSQL("insert into MonsDropItem VALUES ('플라타니스타', '윙프릴의보물')")
                    execSQL("update Maps set mapImgName = 'platanista' where mapName = '모르포시즈 정원'")
                }
            }
        }
        val MIGRATION_1_2_ELSE = object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.apply {
                    execSQL("update Monster set monsType = 'Mini Boss' where monsType = 'named'")
                    execSQL("update Monster set monsType = 'Semi Boss' where monsType = 'boss'")
                    execSQL("update Monster set monsType = 'World Boss' where monsType = 'bigboss'")
                    execSQL("insert into Monster values ('Platanista', 52, 0, 'platanista', 'World Boss')")
                    execSQL("insert into MonsLiveAtMap values ('Platanista', '모르포시즈 정원')")
                    execSQL("insert into MonsDropItem VALUES ('Platanista', 'Surge cycle')")
                    execSQL("insert into MonsDropItem VALUES ('Platanista', 'Scroll S')")
                    execSQL("insert into MonsDropItem VALUES ('Platanista', 'Old Scroll')")
                    execSQL("insert into MonsDropItem VALUES ('Platanista', 'Wingfril''s Treasure')")
                    execSQL("update Maps set mapImgName = 'platanista' where mapName = '모르포시즈 정원'")
                }
            }
        }
    }

    abstract fun getDropListDao() : DropListDao
    abstract fun getCollectionDao() : CollectionDao
    abstract  fun getTimerDao() : TimerDao
}