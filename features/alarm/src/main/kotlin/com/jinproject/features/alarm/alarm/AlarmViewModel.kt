package com.jinproject.features.alarm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.core.util.toEpochMilli
import com.jinproject.design_ui.R
import com.jinproject.domain.usecase.alarm.ManageTimerSettingUsecase
import com.jinproject.domain.usecase.alarm.SetAlarmUsecase
import com.jinproject.features.alarm.alarm.item.MonsterState
import com.jinproject.features.alarm.alarm.item.TimerState
import com.jinproject.features.alarm.alarm.receiver.AlarmReceiver
import com.jinproject.features.alarm.alarm.utils.AlarmItem
import com.jinproject.features.alarm.alarm.utils.makeAlarm
import com.jinproject.features.core.base.item.SnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@Stable
data class AlarmUiState(
    val timerList: List<TimerState>,
    val monsterList: List<MonsterState>,
    val frequentlyUsedBossList: List<String>,
) {
    companion object {
        fun getInitValue() = AlarmUiState(
            timerList = listOf(TimerState.getInitValue()),
            monsterList = emptyList(),
            frequentlyUsedBossList = emptyList(),
        )
    }
}

@Stable
data class AlarmBottomSheetUiState(
    val zonedDateTime: ZonedDateTime,
    val selectedBossName: String
) {
    companion object {
        fun getInitValue() = AlarmBottomSheetUiState(
            zonedDateTime = ZonedDateTime.now(),
            selectedBossName = "",
        )
    }
}

@HiltViewModel
class AlarmViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val timerRepository: com.jinproject.domain.repository.TimerRepository,
    private val dropListRepository: com.jinproject.domain.repository.DropListRepository,
    private val setAlarmUsecase: SetAlarmUsecase,
    private val manageTimerSettingUsecase: ManageTimerSettingUsecase,
) : ViewModel() {

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<AlarmUiState> =
        dropListRepository.getBossMonsterList().map { monsterModelList ->
            monsterModelList.map {
                MonsterState.fromMonsterDomain(it)
            }
        }.flatMapLatest { monsterList ->
            timerRepository.getTimerList()
                .combine(manageTimerSettingUsecase.getTimerSetting()) { timers, setting ->
                    AlarmUiState(
                        timerList = timers.map { TimerState.fromDomain(it) },
                        monsterList = monsterList,
                        frequentlyUsedBossList = setting.frequentlyUsedBossList ?: emptyList()
                    )
                }
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = AlarmUiState.getInitValue(),
        )

    fun addBossToFrequentlyUsedList(bossName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!uiState.value.frequentlyUsedBossList.contains(bossName))
                manageTimerSettingUsecase.updateTimerSetting(
                    ManageTimerSettingUsecase.TimerSetting(
                        frequentlyUsedBossList = uiState.value.frequentlyUsedBossList.toMutableList()
                            .apply { add(bossName) }
                    )
                )
        }
    }

    fun removeBossFromFrequentlyUsedList(bossName: String) {
        viewModelScope.launch(context = Dispatchers.IO) {
            manageTimerSettingUsecase.updateTimerSetting(
                ManageTimerSettingUsecase.TimerSetting(
                    frequentlyUsedBossList = uiState.value.frequentlyUsedBossList.filter { it != bossName }
                )
            )
        }
    }

    fun setAlarm(
        monsterName: String,
        deadTime: ZonedDateTime,
        showSnackBar: suspend (SnackBarMessage) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val timer = setAlarmUsecase(
                monsterName = monsterName,
                deadTime = deadTime,
            )
            val intervals = manageTimerSettingUsecase.getTimerSetting().first().interval
            val monsterImageName = dropListRepository.getMonsInfo(monsterName).first().imageName

            launch(Dispatchers.Main.immediate) {
                with(timer[0]) {
                    alarmManager.makeAlarm(
                        context = context,
                        nextGenTime = dateTime.toEpochMilli(),
                        item = AlarmItem(
                            name = monsterName,
                            imgName = monsterImageName,
                            code = id
                        ),
                        intervalFirstTimerSetting = intervals?.firstInterval ?: 0,
                    )
                }

                with(timer[1]) {
                    alarmManager.makeAlarm(
                        context = context,
                        nextGenTime = dateTime.toEpochMilli(),
                        item = AlarmItem(
                            name = monsterName,
                            imgName = monsterImageName,
                            code = id + 300
                        ),
                        intervalSecondTimerSetting = intervals?.secondInterval ?: 0,
                    )
                }

                showSnackBar(
                    SnackBarMessage(
                        headerMessage = "$monsterName ${
                            context.getString(
                                R.string.alarm_setted
                            )
                        }"
                    )
                )
            }
        }
    }

    fun clearAlarm(code: Int, bossName: String) {
        deleteAlarm(code = code)
        deleteAlarm(code = code + 300)

        deleteTimer(bossName)
    }

    private fun deleteAlarm(code: Int) {
        val notifyIntent = Intent(context, AlarmReceiver::class.java)
        val notifyPendingIntent = PendingIntent.getBroadcast(
            context,
            code,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(notifyPendingIntent)
    }

    private fun deleteTimer(bossName: String) =
        viewModelScope.launch(Dispatchers.IO) { timerRepository.deleteTimer(bossName) }

    fun addOverlayMonster(monsterName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val overlaidMonsterList =
                manageTimerSettingUsecase.getTimerSetting().first().overlaidMonsterList

            val new = if (overlaidMonsterList != null) {
                if (!overlaidMonsterList.contains(monsterName))
                    overlaidMonsterList.toMutableList()
                        .apply { add(monsterName) }
                else
                    return@launch
            } else
                listOf(monsterName)

            manageTimerSettingUsecase.updateTimerSetting(
                ManageTimerSettingUsecase.TimerSetting(
                    overlaidMonsterList = new
                )
            )
        }
    }

    fun removeOverlayMonster(monsterName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val overlaidMonsterList =
                manageTimerSettingUsecase.getTimerSetting()
                    .first().overlaidMonsterList?.toMutableList()

            overlaidMonsterList?.let {
                manageTimerSettingUsecase.updateTimerSetting(
                    ManageTimerSettingUsecase.TimerSetting(overlaidMonsterList = it.apply {
                        remove(
                            monsterName
                        )
                    })
                )
            }
        }
    }
}

