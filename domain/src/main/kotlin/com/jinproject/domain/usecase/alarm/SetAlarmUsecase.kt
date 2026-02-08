package com.jinproject.domain.usecase.alarm

import com.jinproject.domain.repository.DropListRepository
import com.jinproject.domain.repository.TimerRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.inject.Inject

/**
 * 몬스터 알람 유즈케이스
 *
 * 특정 몬스터의 죽은 시간의 입력
 */
class SetAlarmUsecase @Inject constructor(
    private val dropListRepository: DropListRepository,
    private val timerRepository: TimerRepository
) {
    /**
     * 몬스터 알람 기능을 위한 필요한 정보들
     *
     * @param id: 몬스터 알람 식별자
     * @param monsterName:  몬스터 이름
     * @param dateTime: 몬스터 알람이 설정된 DateTime
     */
    data class Timer(
        val id: Int,
        val monsterName: String,
        val dateTime: ZonedDateTime,
    )

    /**
     * @param monsterName : 몬스터 이름
     * @param deadTime : 몬스터 죽은 시간
     * @return (다음 재 생성 시간 - 사용자가 설정한 알람 간격) 의 첫번째와 두번째 알람
     */
    suspend operator fun invoke(
        monsterName: String,
        deadTime: ZonedDateTime,
    ): List<Timer> {
        val monsterInfo =
            dropListRepository.getMonster(monsterName).first()

        timerRepository.getAndSetBossTimerList(
            monsName = monsterName,
            nextSpawnDateTime = monsterInfo.calculateNextSpawnTime(deadTime),
        )

        val timersByInterval = timerRepository.getTimer(monsName = monsterName).first()

        return timersByInterval
    }
}