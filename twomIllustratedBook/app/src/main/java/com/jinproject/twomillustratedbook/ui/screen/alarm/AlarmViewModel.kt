package com.jinproject.twomillustratedbook.ui.screen.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.jinproject.twomillustratedbook.data.repository.DropListRepository
import com.jinproject.twomillustratedbook.data.repository.TimerRepository
import com.jinproject.twomillustratedbook.domain.model.MonsterType
import com.jinproject.twomillustratedbook.domain.model.WeekModel
import com.jinproject.twomillustratedbook.ui.Receiver.AlarmReceiver
import com.jinproject.twomillustratedbook.ui.base.item.SnackBarMessage
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.AlarmItem
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimeState
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimerState
import com.jinproject.twomillustratedbook.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

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
    private val timerRepository: TimerRepository,
    private val dropListRepository: DropListRepository
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
        timerRepository.timerPreferences.onEach { prefs ->
            _uiState.update { state ->
                state.copy(
                    recentlySelectedBossClassified = MonsterType.findByStoredName(prefs.recentlySelectedBossClassified).displayName,
                    recentlySelectedBossName = prefs.recentlySelectedBossName,
                    frequentlyUsedBossList = prefs.frequentlyUsedBossListList
                )
            }
            getBossListByType(MonsterType.findByStoredName(prefs.recentlySelectedBossClassified))
        }.launchIn(viewModelScope)
    }

    fun setRecentlySelectedBossClassified(bossClassified: MonsterType) {
        viewModelScope.launch(Dispatchers.IO) {
            timerRepository.setRecentlySelectedBossClassified(bossClassified)
        }
    }

    private fun getBossListByType(bossClassified: MonsterType) {
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
                emitSnackBar(SnackBarMessage(headerMessage = "오류가 발생했습니다. 다시 시도해 주세요."))
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

    fun setTimer(timerState: TimerState) = viewModelScope.launch(Dispatchers.IO) {
        timerRepository.setTimer(
            timerState.timeState.day.getCodeByWeek(),
            timerState.timeState.hour,
            timerState.timeState.minutes,
            timerState.timeState.seconds,
            timerState.bossName
        )
    }

    fun setOta(ota: Int, bossName: String) =
        viewModelScope.launch(Dispatchers.IO) { timerRepository.setOta(ota, bossName) }

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
        }.zip(timerRepository.timerPreferences) { monsterState, prefs ->
            val genTime = Calendar.getInstance().apply {
                hour = uiState.value.timeState.hour
                minute = uiState.value.timeState.minutes
                second = uiState.value.timeState.seconds
            }.timeInMillis + (monsterState.genTime * 1000).toLong()

            makeAlarm(
                nextGenTime = genTime - prefs.intervalFirstTimerSetting * 60000,
                item = AlarmItem(
                    name = monsterState.name,
                    imgName = monsterState.imgName,
                    code = getAlarmCode(monsterName),
                    gtime = monsterState.genTime
                )
            )

            makeAlarm(
                nextGenTime = genTime - prefs.intervalSecondTimerSetting * 60000,
                item = AlarmItem(
                    name = monsterState.name,
                    imgName = monsterState.imgName,
                    code = getAlarmCode(monsterName) + 300,
                    gtime = monsterState.genTime
                )
            )

            val cal = Calendar.getInstance().apply {
                timeInMillis = genTime
            }

            setTimer(
                TimerState(
                    id = getAlarmCode(monsterName),
                    bossName = monsterName,
                    timeState = TimeState(
                        day = WeekModel.findByCode(cal.day),
                        hour = cal.hour,
                        minutes = cal.minute,
                        seconds = cal.second
                    )
                )
            )

            emitSnackBar(SnackBarMessage(headerMessage = "알람이 설정되었습니다."))
        }.catch { e ->
            when (e) {
                is NoSuchElementException -> {
                    emitSnackBar(
                        SnackBarMessage(
                            headerMessage = "일시적인 장애가 발생하였습니다.",
                            contentMessage = e.message.toString()
                        )
                    )
                }
            }
        }.catch { e ->
            emitSnackBar(SnackBarMessage(headerMessage = "오류가 발생했습니다. 다시 시도해 주세요."))
        }.launchIn(viewModelScope)
    }

    private fun getAlarmCode(monsterName: String): Int {
        var code = 0
        try {
            code = uiState.value.timerList.last().id + 1

            uiState.value.timerList.forEach { timerState ->
                if (timerState.bossName == monsterName) {
                    code = timerState.id
                }
            }
        } catch (e: NoSuchElementException) {
            code = 1
        }

        return code
    }

    fun clearAlarm(code: Int, bossName: String) {
        val notifyIntent = Intent(context, AlarmReceiver::class.java)
        val notifyPendingIntent = PendingIntent.getBroadcast(
            context,
            code,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(notifyPendingIntent)

        deleteTimer(bossName)
    }

    private fun deleteTimer(bossName: String) =
        viewModelScope.launch(Dispatchers.IO) { timerRepository.deleteTimer(bossName) }

    private fun makeAlarm(nextGenTime: Long, item: AlarmItem) {
        val notifyIntentImmediately = Intent(context, AlarmReceiver::class.java)
        notifyIntentImmediately.putExtra("msg", item.name)
        notifyIntentImmediately.putExtra("img", item.imgName)
        notifyIntentImmediately.putExtra("code", item.code)
        notifyIntentImmediately.putExtra("gtime", item.gtime)
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

    private fun emitSnackBar(snackBarMessage: SnackBarMessage) {
        viewModelScope.launch {
            _snackBarMessage.emit(snackBarMessage)
        }
    }
}
