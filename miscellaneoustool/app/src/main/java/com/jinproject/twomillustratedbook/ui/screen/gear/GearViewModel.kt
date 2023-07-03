package com.jinproject.twomillustratedbook.ui.screen.gear

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.usecase.timer.SetIntervalSettingUsecase
import com.jinproject.twomillustratedbook.ui.base.item.SnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class GearUiState(
    val intervalFirstTimer: Int,
    val intervalSecondTimer: Int
) {
    companion object {
        fun getInitValue() = GearUiState(
            intervalFirstTimer = 5,
            intervalSecondTimer = 0
        )
    }
}

@HiltViewModel
class GearViewModel @Inject constructor(
    private val timerRepository: com.jinproject.domain.repository.TimerRepository,
    private val setIntervalSettingUsecase: SetIntervalSettingUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GearUiState.getInitValue())
    val uiState get() = _uiState.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<SnackBarMessage>()
    val snackBarMessage get() = _snackBarMessage.asSharedFlow()

    init {
        getIntervalTimerSettings()
    }

    private fun getIntervalTimerSettings() = timerRepository.getTimerPreferences().onEach { prefs ->
        _uiState.update { state ->
            state.copy(
                intervalFirstTimer = prefs.intervalFirstTimerSetting,
                intervalSecondTimer = prefs.intervalSecondTimerSetting
            )
        }
    }.launchIn(viewModelScope)

    fun setIntervalFirstTimerSetting(minutes: Int) = _uiState.update { state ->
        state.copy(intervalFirstTimer = minutes)
    }

    fun setIntervalSecondTimerSetting(minutes: Int) = _uiState.update { state ->
        state.copy(intervalSecondTimer = minutes)
    }

    fun setIntervalTimerSetting() {
        viewModelScope.launch {
            setIntervalSettingUsecase(
                first = uiState.value.intervalFirstTimer,
                second = uiState.value.intervalSecondTimer
            )
        }
    }

    fun emitSnackBar(snackBarMessage: SnackBarMessage) {
        viewModelScope.launch {
            _snackBarMessage.emit(snackBarMessage)
        }
    }
}