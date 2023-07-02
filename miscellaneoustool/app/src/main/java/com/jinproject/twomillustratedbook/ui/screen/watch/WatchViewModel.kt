package com.jinproject.twomillustratedbook.ui.screen.watch

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.usecase.timer.SetOverlaySettingUsecase
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimerState
import com.jinproject.twomillustratedbook.ui.screen.alarm.mapper.toTimerState
import com.jinproject.twomillustratedbook.ui.screen.watch.item.ButtonStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class WatchUiState(
    val frequentlyUsedBossList: List<String>,
    val selectedMonsterName: String,
    val timerList: List<TimerState>,
    val watchStatus: ButtonStatus,
    val fontSize: Int,
    val xPos:Int,
    val yPos:Int
) {
    companion object {
        fun getInitValue() = WatchUiState(
            frequentlyUsedBossList = emptyList(),
            selectedMonsterName = "",
            timerList = emptyList(),
            watchStatus = ButtonStatus.OFF,
            fontSize = 14,
            xPos = 0,
            yPos = 0
        )
    }
}

@HiltViewModel
class WatchViewModel @Inject constructor(
    private val timerRepository: com.jinproject.domain.repository.TimerRepository,
    private val setOverlaySettingUsecase: SetOverlaySettingUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchUiState.getInitValue())
    val uiState get() = _uiState.asStateFlow()

    init {
        getRecentlySelectedBossInfo()
        getTimerList()
    }

    private fun getRecentlySelectedBossInfo() {
        timerRepository.getTimerPreferences().onEach { prefs ->
            _uiState.update { state ->
                state.copy(
                    frequentlyUsedBossList = prefs.frequentlyUsedBossListList,
                    fontSize = prefs.fontSize,
                    xPos = prefs.xPos,
                    yPos = prefs.yPos
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun getTimerList() = timerRepository.getTimer().map { timerModels ->
        timerModels.filter { timerModel ->
            timerModel.isOverlayOnOrNot
        }.map { timerModel -> timerModel.toTimerState() }
    }.onEach { timerStates ->
        _uiState.update { state ->
            state.copy(timerList = timerStates)
        }
    }.launchIn(viewModelScope)

    fun setWatchStatus(watchStatus: ButtonStatus) = _uiState.update { state ->
        state.copy(watchStatus = watchStatus)
    }

    fun setFontSize(fontSize: Int) = _uiState.update { state ->
        state.copy(fontSize = fontSize)
    }

    fun setXPos(pos: Int) = _uiState.update { state ->
        state.copy(xPos = pos)
    }

    fun setYPos(pos: Int) = _uiState.update { state ->
        state.copy(yPos = pos)
    }

    fun setTimerSetting() {
        viewModelScope.launch {
            setOverlaySettingUsecase.invoke(
                fontSize = uiState.value.fontSize,
                xPos = uiState.value.xPos,
                yPos = uiState.value.yPos
            )
        }
    }

    fun setSelectedMonsterName(monsterName: String) = _uiState.update { state ->
        state.copy(selectedMonsterName = monsterName)
    }

    fun setSelectedMonsterOtaToTrue(value: Int) {
        viewModelScope.launch {
            timerRepository.setOta(value, uiState.value.selectedMonsterName)
        }
    }
}