package com.jinproject.data.database

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jinproject.data.datasource.cache.database.BookDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BookDatabaseTest {
    private val TEST_DB = "db_twom_2.db"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        BookDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        var db = helper.createDatabase(TEST_DB, 1).apply {

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

        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, BookDatabase.MIGRATION_1_2_KOR)
    }
}