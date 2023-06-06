package com.jinproject.twomillustratedbook.ui.screen.watch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.twomillustratedbook.domain.repository.TimerRepository
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.TimerState
import com.jinproject.twomillustratedbook.ui.screen.watch.item.ButtonStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WatchUiState(
    val frequentlyUsedBossList: List<String>,
    val selectedMonsterName: String,
    val timerList: List<TimerState>,
    val watchStatus: ButtonStatus,
    val fontSize: Int
) {
    companion object {
        fun getInitValue() = WatchUiState(
            frequentlyUsedBossList = emptyList(),
            selectedMonsterName = "",
            timerList = emptyList(),
            watchStatus = ButtonStatus.OFF,
            fontSize = 14
        )
    }
}

@HiltViewModel
class WatchViewModel @Inject constructor(
    private val timerRepository: TimerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchUiState.getInitValue())
    val uiState get() = _uiState.asStateFlow()

    init {
        getRecentlySelectedBossInfo()
        getTimerList()
    }

    private fun getRecentlySelectedBossInfo() {
        timerRepository.timerPreferences.onEach { prefs ->
            _uiState.update { state ->
                state.copy(
                    frequentlyUsedBossList = prefs.frequentlyUsedBossListList,
                    fontSize = prefs.fontSize
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

    fun setFontSize(fontSize: Int) {
        viewModelScope.launch {
            timerRepository.setFontSize(fontSize)
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