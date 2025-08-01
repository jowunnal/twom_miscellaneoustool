package com.jinproject.domain.usecase.alarm

import com.jinproject.domain.repository.TimerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ManageTimerSettingUsecase @Inject constructor(
    private val timerRepository: TimerRepository
) {
    /**
     * 몬스터 알람 관련 설정값
     *
     * @param fontSize: 오버레이 폰트 사이즈
     * @param xPos: 오버레이 x좌표
     * @param yPos: 오버레이 y좌표
     * @param frequentlyUsedBossList: 자주 쓰는 몬스터 리스트
     * @param interval: 타이머 간격
     * @param overlaidMonsterList: 오버레이된 몬스터 리스트
     */
    data class TimerSetting(
        val fontSize: Int? = null,
        val xPos: Int? = null,
        val yPos: Int? = null,
        val frequentlyUsedBossList: List<String>? = null,
        val interval: TimerInterval? = null,
        val overlaidMonsterList: List<String>? = null,
    )

    /**
     * 사용자가 설정하는 타이머 간격
     *
     * @param firstInterval: 첫번째 타이머 간격
     * @param secondInterval: 두번째 타이머 간격
     */
    data class TimerInterval(
        val firstInterval: Int? = null,
        val secondInterval: Int? = null,
    ) {
        fun verify(origin: TimerInterval): Boolean =
            if (firstInterval != null && secondInterval == null)
                firstInterval > origin.secondInterval!!
            else if (secondInterval != null && firstInterval == null)
                origin.firstInterval!! > secondInterval
            else if (firstInterval != null && secondInterval != null)
                firstInterval > secondInterval
            else
                true
    }

    suspend fun updateTimerSetting(timerSetting: TimerSetting) {
        val origin = timerRepository.getTimerSetting().first().interval ?: TimerInterval(5, 0)

        if (timerSetting.interval?.verify(origin) == false)
            throw ManageTimerSettingException.InvalidIntervalException("첫번째 타이머 간격[${timerSetting.interval.firstInterval}]은 두번째 타이머 간격[${timerSetting.interval.secondInterval}] 보다 길어야 합니다.")

        timerRepository.updateTimerSetting(timerSetting = timerSetting)
    }

    fun getTimerSetting(): Flow<TimerSetting> = timerRepository.getTimerSetting()

    sealed class ManageTimerSettingException : Exception() {
        data class InvalidIntervalException(override val message: String) :
            ManageTimerSettingException()
    }
}