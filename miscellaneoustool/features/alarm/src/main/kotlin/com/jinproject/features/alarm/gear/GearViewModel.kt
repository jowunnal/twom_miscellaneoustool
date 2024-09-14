package com.jinproject.features.alarm.gear

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.usecase.timer.GetIntervalUsecase
import com.jinproject.domain.usecase.timer.SetIntervalSettingUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val setIntervalSettingUsecase: SetIntervalSettingUsecase,
    private val getIntervalUsecase: GetIntervalUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GearUiState.getInitValue())
    val uiState get() = _uiState.asStateFlow()

    init {
        getIntervalTimerSettings()
    }

    private fun getIntervalTimerSettings() = getIntervalUsecase().onEach { interval ->
        _uiState.update { state ->
            state.copy(
                intervalFirstTimer = interval.first,
                intervalSecondTimer = interval.second
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
}