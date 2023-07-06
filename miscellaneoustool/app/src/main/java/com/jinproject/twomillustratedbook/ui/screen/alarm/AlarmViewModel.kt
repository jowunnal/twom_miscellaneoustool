package com.jinproject.twomillustratedbook.ui.screen.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Stable
import androidx.lifecycle.*
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.base.item.SnackBarMessage
import com.jinproject.twomillustratedbook.ui.receiver.AlarmReceiver
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.AlarmItem
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimeState
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimerState
import com.jinproject.twomillustratedbook.ui.screen.alarm.mapper.toTimerState
import com.jinproject.twomillustratedbook.ui.screen.droplist.mapper.toMonsterState
import com.jinproject.twomillustratedbook.utils.day
import com.jinproject.twomillustratedbook.utils.hour
import com.jinproject.twomillustratedbook.utils.minute
import com.jinproject.twomillustratedbook.utils.second
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@Stable
data class AlarmUiState(
    val timerList: List<TimerState>,
    val timeState: TimeState,
    val selectedBossName: String,
    val recentlySelectedBossClassified: String,
    val recentlySelectedBossName: String,
    val bossNameList: List<String>,
    val frequentlyUsedBossList: List<String>
) {
    companion object {
        fun getInitValue() = AlarmUiState(
            timerList = listOf(TimerState.getInitValue()),
            timeState = TimeState.getInitValue(),
            selectedBossName = "",
            recentlySelectedBossClassified = "",
            recentlySelectedBossName = "",
            bossNameList = emptyList(),
            frequentlyUsedBossList = emptyList()
        )
    }
}

@HiltViewModel
class AlarmViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val timerRepository: com.jinproject.domain.repository.TimerRepository,
    private val dropListRepository: com.jinproject.domain.repository.DropListRepository
) :
    ViewModel() {
    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val _uiState = MutableStateFlow(AlarmUiState.getInitValue())
    val uiState get() = _uiState.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<SnackBarMessage>()
    val snackBarMessage get() = _snackBarMessage.asSharedFlow()

    init {
        getTimerList()
        getRecentlySelectedBossInfo()
    }

    private fun getRecentlySelectedBossInfo() {
        timerRepository.getTimerPreferences().onEach { prefs ->
            _uiState.update { state ->
                state.copy(
                    recentlySelectedBossClassified = context.doOnLocaleLanguage(
                        onKo = com.jinproject.domain.model.MonsterType.findByStoredName(prefs.recentlySelectedBossClassified).displayName,
                        onElse = com.jinproject.domain.model.MonsterType.findByStoredName(prefs.recentlySelectedBossClassified).storedName
                    ),
                    recentlySelectedBossName = prefs.recentlySelectedBossName,
                    frequentlyUsedBossList = prefs.frequentlyUsedBossListList
                )
            }
            getBossListByType(com.jinproject.domain.model.MonsterType.findByStoredName(prefs.recentlySelectedBossClassified))
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

    fun removeBossFromFrequentlyUsedList(bossName: String) {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            if (throwable is IllegalArgumentException) {
                emitSnackBar(SnackBarMessage(headerMessage = context.getString(R.string.message_throw_exceptions)))
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
    }.launchIn(viewModelScope)

    private fun setTimer(timerState: TimerState) {
        viewModelScope.launch(Dispatchers.IO) {
            timerRepository.setTimer(
                timerState.id,
                timerState.timeState.day.getCodeByWeek(),
                timerState.timeState.hour,
                timerState.timeState.minutes,
                timerState.timeState.seconds,
                timerState.bossName
            )
        }
    }

    fun setHourChanged(hour: Int) = _uiState.update { state ->
        state.copy(timeState = uiState.value.timeState.copy(hour = hour))
    }

    fun setMinutesChanged(minutes: Int) = _uiState.update { state ->
        state.copy(timeState = uiState.value.timeState.copy(minutes = minutes))
    }

    fun setSecondsChanged(seconds: Int) = _uiState.update { state ->
        state.copy(timeState = uiState.value.timeState.copy(seconds = seconds))
    }

    fun setSelectedBossName(bossName: String) = _uiState.update { state ->
        state.copy(selectedBossName = bossName)
    }

    @SuppressLint("SimpleDateFormat")
    fun setAlarm(monsterName: String) {
        dropListRepository.getMonsInfo(monsterName).transform { monsterModel ->
            emit(monsterModel.toMonsterState())
        }.zip(timerRepository.getTimerPreferences()) { monsterState, prefs ->
            val genTime = Calendar.getInstance().apply {
                hour = uiState.value.timeState.hour
                minute = uiState.value.timeState.minutes
                second = uiState.value.timeState.seconds
            }.timeInMillis + (monsterState.genTime * 1000).toLong()

            val cal = Calendar.getInstance().apply {
                timeInMillis = genTime
            }

            val timer = uiState.value.timerList.find { timerState ->
                timerState.bossName == monsterName
            }

            val code = timer?.id
                ?: if (uiState.value.timerList.isNotEmpty()) uiState.value.timerList.maxOf { item -> item.id } + 1 else 1

            when (timer) {
                is TimerState -> {
                    timerRepository.updateTimer(
                        id = code,
                        day = cal.day,
                        hour = cal.hour,
                        min = cal.minute,
                        sec = cal.second
                    )
                }
                null -> {
                    setTimer(
                        TimerState(
                            id = code,
                            bossName = monsterName,
                            timeState = TimeState(
                                day = com.jinproject.domain.model.WeekModel.findByCode(cal.day),
                                hour = cal.hour,
                                minutes = cal.minute,
                                seconds = cal.second
                            )
                        )
                    )
                }
            }

            makeAlarm(
                nextGenTime = genTime - prefs.intervalFirstTimerSetting * 60000,
                item = AlarmItem(
                    name = monsterState.name,
                    imgName = monsterState.imgName,
                    code = code,
                    gtime = monsterState.genTime
                ),
                intervalFirstTimerSetting = prefs.intervalFirstTimerSetting
            )

            makeAlarm(
                nextGenTime = genTime - prefs.intervalSecondTimerSetting * 60000,
                item = AlarmItem(
                    name = monsterState.name,
                    imgName = monsterState.imgName,
                    code = code + 300,
                    gtime = monsterState.genTime
                ),
                intervalSecondTimerSetting = prefs.intervalSecondTimerSetting
            )

            emitSnackBar(SnackBarMessage(headerMessage = "$monsterName ${context.getString(R.string.alarm_setted)}"))
        }.catch { e ->
            when (e) {
                is NoSuchElementException -> {
                    emitSnackBar(
                        SnackBarMessage(
                            headerMessage = context.getString(R.string.message_throw_no_such_element),
                            contentMessage = e.message.toString()
                        )
                    )
                }
                else -> emitSnackBar(SnackBarMessage(headerMessage = context.getString(R.string.message_throw_exceptions)))
            }
        }.launchIn(viewModelScope)
    }

    private fun makeAlarm(
        nextGenTime: Long,
        item: AlarmItem,
        intervalFirstTimerSetting: Int = 0,
        intervalSecondTimerSetting: Int = 0
    ) {
        val notifyIntentImmediately = Intent(context, AlarmReceiver::class.java)
        notifyIntentImmediately.putExtra("msg", item.name)
        notifyIntentImmediately.putExtra("img", item.imgName)
        notifyIntentImmediately.putExtra("code", item.code)
        notifyIntentImmediately.putExtra("gtime", item.gtime)
        notifyIntentImmediately.putExtra("first", intervalFirstTimerSetting)
        notifyIntentImmediately.putExtra("second", intervalSecondTimerSetting)

        val notifyPendingIntent = PendingIntent.getBroadcast(
            context,
            item.code,
            notifyIntentImmediately,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(
                nextGenTime,
                notifyPendingIntent
            ), notifyPendingIntent
        )
    }

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

    private fun emitSnackBar(snackBarMessage: SnackBarMessage) {
        viewModelScope.launch {
            _snackBarMessage.emit(snackBarMessage)
        }
    }
}
