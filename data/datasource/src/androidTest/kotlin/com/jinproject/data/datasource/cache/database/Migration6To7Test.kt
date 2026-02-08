package com.jinproject.data.datasource.cache.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class Migration6To7Test {

    companion object {
        private const val TEST_DB = "migration-test"
    }

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        BookDatabase::class.java,
    )

    @Test
    @Throws(IOException::class)
    fun migrate6To7_KOR() {
        // Version 6 DB 생성
        helper.createDatabase(TEST_DB, 6).apply {
            close()
        }

        // Version 7로 마이그레이션 실행 및 검증
        helper.runMigrationsAndValidate(TEST_DB, 7, true, BookMigration.MIGRATION_6_7_KOR)
    }

    @Test
    @Throws(IOException::class)
    fun migrate6To7_ELSE() {
        // Version 6 DB 생성
        helper.createDatabase(TEST_DB, 6).apply {
            close()
        }

        // Version 7로 마이그레이션 실행 및 검증
        helper.runMigrationsAndValidate(TEST_DB, 7, true, BookMigration.MIGRATION_6_7_ELSE)
    }
}