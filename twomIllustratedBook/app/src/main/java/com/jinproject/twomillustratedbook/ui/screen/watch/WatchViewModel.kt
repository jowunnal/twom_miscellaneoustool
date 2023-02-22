package com.jinproject.twomillustratedbook.ui.screen.watch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.twomillustratedbook.data.repository.TimerRepository
import com.jinproject.twomillustratedbook.ui.screen.watch.item.ButtonStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WatchUiState(
    val frequentlyUsedBossList: List<String>,
    val watchStatus: ButtonStatus,
    val fontSize: Int
) {
    companion object {
        fun getInitValue() = WatchUiState(
            frequentlyUsedBossList = emptyList(),
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

    fun setWatchStatus(watchStatus: ButtonStatus) = _uiState.update { state ->
        state.copy(watchStatus = watchStatus)
    }

    fun setFontSize(fontSize: Int) {
        viewModelScope.launch {
            timerRepository.setFontSize(fontSize)
        }
    }
}