package com.jinproject.domain.usecase.alarm

import com.jinproject.domain.entity.Monster
import com.jinproject.domain.entity.MonsterType
import com.jinproject.domain.repository.DropListRepository
import com.jinproject.domain.repository.TimerRepository
import com.jinproject.domain.usecase.repository.FakeTimerRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class SetAlarmUsecaseTest : BehaviorSpec() {
    private val timerRepository: TimerRepository = FakeTimerRepository()
    private val dropListRepository: DropListRepository = mockk(relaxed = true)
    private val setAlarmUsecase: SetAlarmUsecase = SetAlarmUsecase(
        dropListRepository = dropListRepository,
        timerRepository = timerRepository
    )

    init {
        runTest {
            given("알람 간격이 첫번째 0분, 두번째 5분이고, 0시 0분 0초에 불도저 알람을 설정할 때") {
                val current = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)
                val monsterInfo = Monster(
                    name = "불도저",
                    level = 7,
                    genTime = 900,
                    imageName = "bulldozer",
                    type = MonsterType.Named("bulldozer"),
                    dropItems = emptyList(),
                    existedMap = emptyList(),
                    hp = 0
                )
                timerRepository.updateTimerSetting(
                    ManageTimerSettingUsecase.TimerSetting(
                        interval = ManageTimerSettingUsecase.TimerInterval(
                            firstInterval = 0,
                            secondInterval = 5,
                        )
                    )
                )
                every { dropListRepository.getMonsInfo(any()) } returns flowOf(monsterInfo)


                `when`("알람을 설정 하면") {
                    val nextSpawnTimes = setAlarmUsecase(
                        monsterName = monsterInfo.name,
                        deadTime = current,
                    )

                    then("알람 간격은 null 이 아니며, 첫번째 알람은 0시 15분 0초, 두번째 알람은 0시 10분 0초에 울린다.") {
                        val interval = timerRepository.getTimerSetting().first().interval

                        interval shouldNotBe null
                        nextSpawnTimes[0].dateTime shouldBe current.withHour(0).withMinute(15)
                            .withSecond(0)
                        nextSpawnTimes[1].dateTime shouldBe current.withHour(0).withMinute(10)
                            .withSecond(0)
                    }
                }
            }
        }
    }
}