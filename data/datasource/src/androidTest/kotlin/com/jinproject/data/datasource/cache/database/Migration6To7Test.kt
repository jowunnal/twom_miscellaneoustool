package com.jinproject.data.datasource.cache.database

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        // Version 6 DB 생성 및 샘플 데이터 추가
        helper.createDatabase(TEST_DB, 6).apply {
            // 기존 Item 데이터 추가 (장신구 타입 포함)
            execSQL("INSERT INTO Item (itemName, itemType, itemPrice) VALUES ('어금니고리', '장신구', 0)")
            execSQL("INSERT INTO Item (itemName, itemType, itemPrice) VALUES ('불도저망토', '장신구', 0)")
            execSQL("INSERT INTO Item (itemName, itemType, itemPrice) VALUES ('수호병의손껍질', '잡탬', 0)")

            // 기존 Equipment 데이터 (장신구가 아닌 것들)
            execSQL("INSERT INTO Equipment (name, level, img_name) VALUES ('버닝블레이드', 46, 'burning_blade')")
            execSQL("INSERT INTO Equipment (name, level, img_name) VALUES ('인퀴지션', 48, 'inquisition')")

            close()
        }

        // Version 7로 마이그레이션 실행 및 검증
        val db = helper.runMigrationsAndValidate(TEST_DB, 7, true, BookMigration.MIGRATION_6_7_KOR)

        // 마이그레이션 후 검증
        db.apply {
            // 1. 장신구가 Equipment에 추가되었는지 확인
            query("SELECT COUNT(*) FROM Equipment WHERE name IN ('어금니고리', '불도저망토')").use { cursor ->
                cursor.moveToFirst()
                assertEquals("장신구가 Equipment에 추가되어야 함", 2, cursor.getInt(0))
            }

            // 2. 브레이커 장비가 추가되었는지 확인
            query("SELECT COUNT(*) FROM Equipment WHERE name = '나무의 호신부'").use { cursor ->
                cursor.moveToFirst()
                assertEquals("나무의 호신부가 Equipment에 추가되어야 함", 1, cursor.getInt(0))
            }

            // 3. 나무의 호신부 level 확인
            query("SELECT level FROM Equipment WHERE name = '나무의 호신부'").use { cursor ->
                cursor.moveToFirst()
                assertEquals("나무의 호신부 level은 5여야 함", 5, cursor.getInt(0))
            }

            // 4. 수호병의손껍질 추가 확인
            query("SELECT level FROM Equipment WHERE name = '수호병의손껍질'").use { cursor ->
                cursor.moveToFirst()
                assertEquals("수호병의손껍질 level은 46이어야 함", 46, cursor.getInt(0))
            }

            // 5. 새로 추가된 Item에 img_name 컬럼이 있는지 확인
            query("SELECT img_name FROM Item WHERE itemName = '나무의 호신부'").use { cursor ->
                assertTrue("나무의 호신부가 Item에 추가되어야 함", cursor.moveToFirst())
            }

            // 6. 기존 Equipment 데이터 유지 확인
            query("SELECT level FROM Equipment WHERE name = '버닝블레이드'").use { cursor ->
                cursor.moveToFirst()
                assertEquals("버닝블레이드 level은 유지되어야 함", 46, cursor.getInt(0))
            }

            close()
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate6To7_ELSE() {
        // Version 6 DB 생성 및 샘플 데이터 추가
        helper.createDatabase(TEST_DB, 6).apply {
            // 기존 Item 데이터 추가 (accessories 타입 포함)
            execSQL("INSERT INTO Item (itemName, itemType, itemPrice) VALUES ('Fang Ring', 'accessories', 0)")
            execSQL("INSERT INTO Item (itemName, itemType, itemPrice) VALUES ('Bulldozer Cape', 'accessories', 0)")

            // 기존 Equipment 데이터
            execSQL("INSERT INTO Equipment (name, level, img_name) VALUES ('Burning Blade', 46, 'burning_blade')")

            close()
        }

        // Version 7로 마이그레이션 실행 및 검증
        val db = helper.runMigrationsAndValidate(TEST_DB, 7, true, BookMigration.MIGRATION_6_7_ELSE)

        db.apply {
            // 1. accessories가 Equipment에 추가되었는지 확인
            query("SELECT COUNT(*) FROM Equipment WHERE name IN ('Fang Ring', 'Bulldozer Cape')").use { cursor ->
                cursor.moveToFirst()
                assertEquals("accessories가 Equipment에 추가되어야 함", 2, cursor.getInt(0))
            }

            // 2. 브레이커 장비가 추가되었는지 확인
            query("SELECT level FROM Equipment WHERE name = 'Wooden Guard'").use { cursor ->
                cursor.moveToFirst()
                assertEquals("Wooden Guard level은 5여야 함", 5, cursor.getInt(0))
            }

            // 3. 기존 Equipment 데이터 유지 확인
            query("SELECT level FROM Equipment WHERE name = 'Burning Blade'").use { cursor ->
                cursor.moveToFirst()
                assertEquals("Burning Blade level은 유지되어야 함", 46, cursor.getInt(0))
            }

            close()
        }
    }

    @Test
    @Throws(IOException::class)
    fun migrate6To7_KOR_allBreakerEquipmentAdded() {
        // Version 6 DB 생성
        helper.createDatabase(TEST_DB, 6).apply {
            close()
        }

        // Version 7로 마이그레이션 실행
        val db = helper.runMigrationsAndValidate(TEST_DB, 7, true, BookMigration.MIGRATION_6_7_KOR)

        // 모든 브레이커 장비가 추가되었는지 확인
        db.apply {
            val expectedEquipments = listOf(
                // 호신부
                "나무의 호신부", "산호의 호신부", "우파루파의 호신부", "정화의 호신부", "서리의 호신부",
                "폭풍의 호신부", "붉은 호신부", "나비의 호신부", "왕관바위의 호신부", "무한의 유물",
                // 장갑
                "혹한의 장갑", "번개 장갑", "구름 장갑", "정령 장갑",
                // 로브
                "견습 브레이커 로브", "가죽 로브", "우파 비늘 로브", "튼튼한 우파 로브", "산호 로브",
                "등대 로브", "노란 유채 로브", "전투 로브", "번개 로브", "혹한의 로브", "툴레의 로브",
                "망각의 로브", "세계수 로브", "정점의 길",
                // 너클 & 톤파
                "녹슨 너클", "기사 너클", "나무 톤파", "정찰 너클", "무딘 너클", "불도저 톤파",
                "훈련 너클", "산호 너클", "그림자 너클", "브레이커의 너클", "빛나는 너클", "낡은 너클",
                "돌 톤파", "뼈 톤파", "철 톤파", "폭풍 채찍", "전투 너클", "철권", "회색 톤파",
                "글라시아", "스톰브레이커", "거친 너클", "황혼의 송곳니", "강철 톤파", "썬더스틸",
                "강철 너클", "전쟁 너클", "체인 너클", "분쇄 주먹", "서리 발톱", "불굴", "룬 톤파",
                "심연의 파편", "영원", "호랑이 주먹",
                // 모자
                "브레이커 모자", "튼튼한 우파 모자", "뼈 모자", "노란 유채 모자", "서리빛 두건",
                "구름 두건", "훈련 모자", "툴레의 모자",
                // 벨트
                "힘의 벨트", "생각의 속도", "공허의 영역",
                // 부츠
                "노란 유채 부츠", "구름 부츠", "혹한의 부츠", "툴레의 부츠", "바다 부츠", "극한의 발걸음",
                // 수호병의손껍질
                "수호병의손껍질"
            )

            for (equipName in expectedEquipments) {
                query("SELECT COUNT(*) FROM Equipment WHERE name = ?", arrayOf(equipName)).use { cursor ->
                    cursor.moveToFirst()
                    assertEquals("$equipName 이(가) Equipment에 추가되어야 함", 1, cursor.getInt(0))
                }
            }

            close()
        }
    }
}