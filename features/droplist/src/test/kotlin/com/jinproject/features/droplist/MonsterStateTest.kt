package com.jinproject.features.droplist

import com.jinproject.domain.entity.MonsterType
import com.jinproject.features.droplist.state.MonsterState
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.collections.immutable.persistentListOf

class MonsterStateDisplayingGenTimeTest : BehaviorSpec() {
    init {
        val monsterState = MonsterState(
            name = "불도저",
            level = 7,
            genTime = 0,
            imageName = "bulldozer",
            type = MonsterType.Named("bulldozer"),
            items = persistentListOf()
        )

        given("젠타임이 500000초 일 때") {
            val genTime = 500000
            val monster = monsterState.copy(genTime = genTime)

            `when`("젠타임을 화면에 보여줄 때") {
                val displayedText = monster.displayGenTime()

                then("5일 18시간 53분 20초 이다.") {
                    displayedText shouldBe "5일 18시간 53분 20초"
                }
            }
        }

        given("젠타임이 5000초 일 때") {
            val genTime = 5000
            val monster = monsterState.copy(genTime = genTime)

            `when`("젠타임을 화면에 보여줄 때") {
                val displayedText = monster.displayGenTime()

                then("1시간 23분 20초 이다.") {
                    displayedText shouldBe "1시간 23분 20초"
                }
            }
        }

        given("젠타임이 604800초 일 때") {
            val genTime = 604800
            val monster = monsterState.copy(genTime = genTime)

            `when`("젠타임을 화면에 보여줄 때") {
                val displayedText = monster.displayGenTime()

                then("7일 이다.") {
                    displayedText.trim() shouldBe "7일"
                }
            }
        }
    }
}