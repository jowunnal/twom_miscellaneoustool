package com.jinproject.features.alarm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.design_ui.R
import com.jinproject.domain.usecase.alarm.SetAlarmUsecase
import com.jinproject.domain.usecase.timer.GetAlarmStoredBossUsecase
import com.jinproject.features.alarm.alarm.item.AlarmItem
import com.jinproject.features.alarm.alarm.item.TimeState
import com.jinproject.features.alarm.alarm.item.TimerState
import com.jinproject.features.alarm.alarm.mapper.toTimerState
import com.jinproject.features.alarm.alarm.receiver.AlarmReceiver
import com.jinproject.features.alarm.alarm.utils.makeAlarm
import com.jinproject.features.core.base.item.SnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class AlarmUiState(
    val timerList: List<TimerState>,
    val recentlySelectedBossClassified: String,
    val recentlySelectedBossName: String,
    val bossNameList: List<String>,
    val frequentlyUsedBossList: List<String>
) {
    companion object {
        fun getInitValue() = AlarmUiState(
            timerList = listOf(TimerState.getInitValue()),
            recentlySelectedBossClassified = "",
            recentlySelectedBossName = "",
            bossNameList = emptyList(),
            frequentlyUsedBossList = emptyList()
        )
    }
}

@Stable
data class AlarmBottomSheetUiState(
    val timeState: TimeState,
    val selectedBossName: String
) {
    companion object {
        fun getInitValue() = AlarmBottomSheetUiState(
            timeState = TimeState.getInitValue(),
            selectedBossName = ""
        )
    }
}

@HiltViewModel
class AlarmViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val timerRepository: com.jinproject.domain.repository.TimerRepository,
    private val dropListRepository: com.jinproject.domain.repository.DropListRepository,
    private val setAlarmUsecase: SetAlarmUsecase,
    private val getAlarmStoredBossUsecase: GetAlarmStoredBossUsecase
) : ViewModel() {

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val _uiState = MutableStateFlow(AlarmUiState.getInitValue())
    val uiState get() = _uiState.asStateFlow()

    private val _bottomSheetUiState = MutableStateFlow(AlarmBottomSheetUiState.getInitValue())
    val bottomSheetUiState get() = _bottomSheetUiState.asStateFlow()

    init {
        getTimerList()
        getRecentlySelectedBossInfo()
    }

    private fun getRecentlySelectedBossInfo() {
        getAlarmStoredBossUsecase().onEach { alarmStoredBoss ->
            _uiState.update { state ->
                state.copy(
                    recentlySelectedBossClassified = alarmStoredBoss.classified,
                    recentlySelectedBossName = alarmStoredBoss.name,
                    frequentlyUsedBossList = alarmStoredBoss.list
                )
            }
            val monsterType = kotlin.runCatching {
                com.jinproject.domain.model.MonsterType.findByBossTypeName(alarmStoredBoss.classified)
            }.getOrDefault(
                com.jinproject.domain.model.MonsterType.Normal(
                    context.doOnLocaleLanguage(
                        onKo = "일반",
                        onElse = "Normal"
                    )
                )
            )
            getBossListByType(monsterType)
        }.launchIn(viewModelScope)
    }

    fun setRecentlySelectedBossClassified(bossClassified: com.jinproject.domain.model.MonsterType) {
        viewModelScope.launch(Dispatchers.IO) {
            timerRepository.setRecentlySelectedBossClassified(bossClassified)
        }
    }

    private fun getBossListByType(bossClassified: com.jinproject.domain.model.MonsterType) {
        dropListRepository.getMonsterByType(bossClassified).onEach { monsterModels ->
            _uiState.update { state ->
                state.copy(bossNameList = monsterModels.map { monsterModel ->
                    monsterModel.name
                })
            }
        }.catch {

        }.launchIn(viewModelScope)
    }

    fun setRecentlySelectedBossName(bossName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            timerRepository.setRecentlySelectedBossName(bossName)
        }
    }

    fun addBossToFrequentlyUsedList(bossName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!uiState.value.frequentlyUsedBossList.contains(bossName))
                timerRepository.addBossToFrequentlyUsedList(
                    bossName = bossName
                )
        }
    }

    fun removeBossFromFrequentlyUsedList(
        bossName: String,
        showSnackBar: suspend (SnackBarMessage) -> Unit
    ) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            if (throwable is IllegalArgumentException) {
                viewModelScope.launch {
                    showSnackBar(
                        SnackBarMessage(
                            headerMessage = context.getString(
                                R.string.message_throw_exceptions
                            )
                        )
                    )
                }
            }
        }
        viewModelScope.launch(context = Dispatchers.IO + exceptionHandler) {
            timerRepository.setBossToFrequentlyUsedList(
                bossList = uiState.value.frequentlyUsedBossList.toMutableList()
                    .apply { remove(bossName) }
            )
        }
    }

    private fun getTimerList() = timerRepository.getTimer().onEach { timerModels ->
        _uiState.update { state ->
            state.copy(timerList = timerModels.map { timerModel ->
                timerModel.toTimerState()
            })
        }
    }.catch { e ->
        when (e) {
            is IllegalStateException -> {
                _uiState.update { state ->
                    state.copy(
                        timerList = listOf(
                            TimerState(
                                id = 0,
                                bossName = "실패",
                                timeState = TimeState.getInitValue()
                            )
                        )
                    )
                }
            }
        }
    }.launchIn(viewModelScope)

    fun setHourChanged(hour: Int) = _bottomSheetUiState.update { state ->
        state.copy(timeState = bottomSheetUiState.value.timeState.copy(hour = hour))
    }

    fun setMinutesChanged(minutes: Int) = _bottomSheetUiState.update { state ->
        state.copy(timeState = bottomSheetUiState.value.timeState.copy(minutes = minutes))
    }

    fun setSecondsChanged(seconds: Int) = _bottomSheetUiState.update { state ->
        state.copy(timeState = bottomSheetUiState.value.timeState.copy(seconds = seconds))
    }

    fun setSelectedBossName(bossName: String) = _bottomSheetUiState.update { state ->
        state.copy(selectedBossName = bossName, timeState = TimeState.getInitValue())
    }

    fun setAlarm(monsterName: String, showSnackBar: suspend (SnackBarMessage) -> Unit) =
        setAlarmUsecase.invoke(
            monsterName = monsterName,
            monsDiedHour = bottomSheetUiState.value.timeState.hour,
            monsDiedMin = bottomSheetUiState.value.timeState.minutes,
            monsDiedSec = bottomSheetUiState.value.timeState.seconds,
            makeAlarm = { firstInterval, secondInterval, monsterAlarmModel ->

                alarmManager.makeAlarm(
                    context = context,
                    nextGenTime = monsterAlarmModel.nextGtime - firstInterval * 60000,
                    item = AlarmItem(
                        name = monsterAlarmModel.name,
                        imgName = monsterAlarmModel.img,
                        code = monsterAlarmModel.code
                    ),
                    intervalFirstTimerSetting = firstInterval,
                )

                alarmManager.makeAlarm(
                    context = context,
                    nextGenTime = monsterAlarmModel.nextGtime - secondInterval * 60000,
                    item = AlarmItem(
                        name = monsterAlarmModel.name,
                        imgName = monsterAlarmModel.img,
                        code = monsterAlarmModel.code + 300
                    ),
                    intervalSecondTimerSetting = secondInterval,
                )
            }
        ).onEach {
            showSnackBar(
                SnackBarMessage(
                    headerMessage = "$monsterName ${
                        context.getString(
                            R.string.alarm_setted
                        )
                    }"
                )
            )
        }.catch { e ->
            when (e) {
                is NoSuchElementException -> {
                    showSnackBar(
                        SnackBarMessage(
                            headerMessage = context.getString(R.string.message_throw_no_such_element),
                            contentMessage = e.message.toString()
                        )
                    )
                }

                else -> showSnackBar(
                    SnackBarMessage(
                        headerMessage = context.getString(R.string.message_throw_exceptions)
                    )
                )
            }
        }.launchIn(viewModelScope)

    fun clearAlarm(code: Int, bossName: String) {
        deleteAlarm(code)
        deleteAlarm(code + 300)

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
}
