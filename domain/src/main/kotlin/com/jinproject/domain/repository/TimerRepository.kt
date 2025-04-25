package com.jinproject.domain.repository

import com.jinproject.domain.usecase.alarm.SetAlarmUsecase
import com.jinproject.domain.usecase.alarm.ManageTimerSettingUsecase
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.ZonedDateTime

interface TimerRepository {

    suspend fun deleteTimer(monsName: String)

    /**
     * @param filterOverlaying 오버레이 목록으로 필터링 유무
     * @return 등록된 알람 목록
     */
    fun getTimerList(filterOverlaying: Boolean = false): Flow<List<SetAlarmUsecase.Timer>>

    suspend fun getAndSetBossTimerList(monsName: String, nextSpawnDateTime: ZonedDateTime)
    fun getTimerSetting(): Flow<ManageTimerSettingUsecase.TimerSetting>
    suspend fun updateTimerSetting(timerSetting: ManageTimerSettingUsecase.TimerSetting)

    /**
     * 사용자 설정 알람 간격 처리된 타이머
     *
     * @return (재 생성 시간 - 사용자 설정 알람 간격) 의 첫번째 알람과 두번째 알람
     */
    fun getTimer(monsName: String): Flow<List<SetAlarmUsecase.Timer>>

}