package com.jinproject.data.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 3, exportSchema = true
)
abstract class BookDatabase : RoomDatabase() {

    companion object {
        val MIGRATION_1_2_KOR = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.apply {
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
        val MIGRATION_1_2_ELSE = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.apply {
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

        val MIGRATION_2_3_KOR = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.apply {
                    execSQL("insert into Item values ('버닝블레이드', 'weapon', 0)")
                    execSQL("insert into Item values ('임페리얼보우', 'weapon', 0)")
                    execSQL("insert into Item values ('파괴의홀', 'weapon', 0)")
                    execSQL("insert into Item values ('인퀴지션', 'weapon', 0)")
                    execSQL("insert into Item values ('페일노트', 'weapon', 0)")
                    execSQL("insert into Item values ('소울이터', 'weapon', 0)")
                    execSQL("CREATE TABLE ItemInfo ('item_name' TEXT NOT NULL, 'type' TEXT NOT NULL, 'value' REAL NOT NULL, PRIMARY KEY('item_name','type'), FOREIGN KEY('item_name') REFERENCES 'Item'('itemName'))")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('버닝블레이드', 'SPEED', '1.2')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('버닝블레이드', 'MINDAMAGE', '10.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('버닝블레이드', 'MAXDAMAGE', '70.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('버닝블레이드', 'CRI', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('버닝블레이드', 'STATSTR', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('버닝블레이드', 'HR', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('인퀴지션', 'SPEED', '1.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('인퀴지션', 'MINDAMAGE', '11.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('인퀴지션', 'MAXDAMAGE', '77.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('인퀴지션', 'CRI', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('인퀴지션', 'STATSTR', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('인퀴지션', 'HR', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('임페리얼보우', 'SPEED', '2.3')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('임페리얼보우', 'MINDAMAGE', '6.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('임페리얼보우', 'MAXDAMAGE', '54.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('임페리얼보우', 'CRI', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('임페리얼보우', 'STATDEX', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('임페리얼보우', 'MOVE', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('페일노트', 'SPEED', '1.8')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('페일노트', 'MINDAMAGE', '7.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('페일노트', 'MAXDAMAGE', '63.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('페일노트', 'CRI', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('페일노트', 'STATDEX', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('페일노트', 'MOVE', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('파괴의홀', 'SPEED', '4.3')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('파괴의홀', 'MINDAMAGE', '6.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('파괴의홀', 'MAXDAMAGE', '60.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('파괴의홀', 'STATINT', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('파괴의홀', 'MP', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('파괴의홀', 'MPREGEN', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('소울이터', 'SPEED', '4.3')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('소울이터', 'MINDAMAGE', '6.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('소울이터', 'MAXDAMAGE', '72.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('소울이터', 'STATINT', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('소울이터', 'MP', '0.0')")

                    execSQL("CREATE TABLE Equipment ('name' TEXT NOT NULL, 'level' INTEGER NOT NULL, 'img_name' TEXT NOT NULL, PRIMARY KEY('name'), FOREIGN KEY('name') REFERENCES 'Item'('itemName'))")
                    execSQL("insert into Equipment select Item.itemName, 0, ' ' from Item where Item.itemType like 'weapon' or Item.itemType like 'armor'")
                    execSQL("update Equipment set level = 46 where Equipment.name like '임페리얼보우' or Equipment.name like '버닝블레이드' or Equipment.name like '파괴의홀' ")
                    execSQL("update Equipment set level = 48 where Equipment.name like '페일노트' or Equipment.name like '인퀴지션' or Equipment.name like '소울이터' ")
                    execSQL("update Equipment set img_name = 'imperial_bow' where Equipment.name like '임페리얼보우'")
                    execSQL("update Equipment set img_name = 'burning_blade' where Equipment.name like '버닝블레이드'")
                    execSQL("update Equipment set img_name = 'hole_of_destruction' where Equipment.name like '파괴의홀'")
                    execSQL("update Equipment set img_name = 'fail_not' where Equipment.name like '페일노트'")
                    execSQL("update Equipment set img_name = 'inquisition' where Equipment.name like '인퀴지션'")
                    execSQL("update Equipment set img_name = 'soul_eater' where Equipment.name like '소울이터'")
                }
            }
        }

        val MIGRATION_2_3_ELSE = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.apply {
                    execSQL("insert into Item values ('Burning Blade', 'weapon', 0)")
                    execSQL("insert into Item values ('Inquisition', 'weapon', 0)")
                    execSQL("insert into Item values ('Imperial Bow', 'weapon', 0)")
                    execSQL("insert into Item values ('Fail-Not', 'weapon', 0)")
                    execSQL("insert into Item values ('Hole of Destruction', 'weapon', 0)")
                    execSQL("insert into Item values ('Soul Eater', 'weapon', 0)")
                    execSQL("CREATE TABLE 'ItemInfo' ('item_name' TEXT, 'type' TEXT, 'value' REAL, PRIMARY KEY('item_name','type'), FOREIGN KEY('item_name') REFERENCES 'Item'('itemName'))")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Burning Blade', 'SPEED', '1.2')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Burning Blade', 'MINDAMAGE', '10.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Burning Blade', 'MAXDAMAGE', '70.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Burning Blade', 'CRI', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Burning Blade', 'STATSTR', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Burning Blade', 'HR', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Inquisition', 'SPEED', '1.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Inquisition', 'MINDAMAGE', '11.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Inquisition', 'MAXDAMAGE', '77.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Inquisition', 'CRI', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Inquisition', 'STATSTR', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Inquisition', 'HR', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Imperial Bow', 'SPEED', '2.3')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Imperial Bow', 'MINDAMAGE', '6.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Imperial Bow', 'MAXDAMAGE', '54.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Imperial Bow', 'CRI', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Imperial Bow', 'STATDEX', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Imperial Bow', 'MOVE', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Fail-Not', 'SPEED', '1.8')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Fail-Not', 'MINDAMAGE', '7.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Fail-Not', 'MAXDAMAGE', '63.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Fail-Not', 'CRI', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Fail-Not', 'STATDEX', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Fail-Not', 'MOVE', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Hole of Destruction', 'SPEED', '4.3')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Hole of Destruction', 'MINDAMAGE', '6.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Hole of Destruction', 'MAXDAMAGE', '60.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Hole of Destruction', 'STATINT', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Hole of Destruction', 'MP', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Hole of Destruction', 'MPREGEN', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Soul Eater', 'SPEED', '4.3')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Soul Eater', 'MINDAMAGE', '6.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Soul Eater', 'MAXDAMAGE', '72.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Soul Eater', 'STATINT', '0.0')")
                    execSQL("INSERT INTO ItemInfo ('item_name', 'type', 'value') VALUES ('Soul Eater', 'MP', '0.0')")

                    execSQL("CREATE TABLE Equipment ('name' TEXT, 'level' INTEGER, 'img_name' TEXT, PRIMARY KEY('name'), FOREIGN KEY('name') REFERENCES 'Item'('itemName'))")
                    execSQL("insert into Equipment select Item.itemName, 0 from Item where Item.itemType like 'weapon' or Item.itemType like 'armor'")
                    execSQL("update Equipment set level = 46 where Equipment.name like 'Imperial Bow' or Equipment.name like 'Burning Blade' or Equipment.name like 'Hole of Destruction' ")
                    execSQL("update Equipment set level = 48 where Equipment.name like 'Fail-Not' or Equipment.name like 'Inquisition' or Equipment.name like 'Soul Eater' ")
                    execSQL("update Equipment set img_name = 'imperial_bow' where Equipment.name like 'Imperial Bow'")
                    execSQL("update Equipment set img_name = 'burning_blade' where Equipment.name like 'Burning Blade'")
                    execSQL("update Equipment set img_name = 'hole_of_destruction' where Equipment.name like 'Hole of Destruction'")
                    execSQL("update Equipment set img_name = 'fail_not' where Equipment.name like 'Fail-Not'")
                    execSQL("update Equipment set img_name = 'inquisition' where Equipment.name like 'Inquisition'")
                    execSQL("update Equipment set img_name = 'soul_eater' where Equipment.name like 'Soul Eater'")
                }
            }
        }
    }

    abstract fun getDropListDao(): DropListDao
    abstract fun getCollectionDao(): CollectionDao
    abstract fun getTimerDao(): TimerDao
    abstract fun getSimulatorDao(): SimulatorDao
}