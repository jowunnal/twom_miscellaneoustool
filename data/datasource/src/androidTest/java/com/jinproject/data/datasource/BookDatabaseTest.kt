package com.jinproject.data.database

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jinproject.data.datasource.cache.database.BookDatabase
import com.jinproject.data.datasource.cache.database.BookMigration.MIGRATION_1_2_KOR
import com.jinproject.data.datasource.cache.database.BookMigration.MIGRATION_2_3_KOR
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BookDatabaseTest {
    private val TEST_DB_VER2 = "db_twom_2.db"
    private val TEST_DB_VER3 = "db_twom_3.db"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        BookDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        var db = helper.createDatabase(TEST_DB_VER2, 1).apply {

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

            close()
        }

        db = helper.runMigrationsAndValidate(TEST_DB_VER2, 2, true, MIGRATION_1_2_KOR)
    }

    @Test
    @Throws(IOException::class)
    fun migrate2To3() {
        var db = helper.createDatabase(TEST_DB_VER2, 2).apply {

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

            close()
        }

        db = helper.runMigrationsAndValidate(
            TEST_DB_VER3,
            3,
            true,
            MIGRATION_2_3_KOR
        )
    }
}