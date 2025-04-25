package com.jinproject.features.alarm.watch

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.usecase.alarm.ManageTimerSettingUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
data class SettingUiState(
    val fontSize: Int,
    val xPos: Int,
    val yPos: Int,
    val firstInterval: Int,
    val secondInterval: Int,
) {
    companion object {
        fun getInitValue() = SettingUiState(
            fontSize = 0,
            xPos = 0,
            yPos = 0,
            firstInterval = 0,
            secondInterval = 0,
        )
    }
}

@HiltViewModel
class AlarmSettingViewModel @Inject constructor(
    timerRepository: com.jinproject.domain.repository.TimerRepository,
    private val manageTimerSettingUsecase: ManageTimerSettingUsecase,
) : ViewModel() {

    val uiState: StateFlow<SettingUiState> =
        manageTimerSettingUsecase.getTimerSetting()
            .combine(timerRepository.getTimerList(true)) { setting, timers ->
                SettingUiState(
                    fontSize = setting.fontSize ?: 0,
                    xPos = setting.xPos ?: 0,
                    yPos = setting.yPos ?: 0,
                    firstInterval = setting.interval?.firstInterval ?: 0,
                    secondInterval = setting.interval?.secondInterval ?: 0,
                )
            }.stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5_000),
                initialValue = SettingUiState.getInitValue(),
            )

    fun setTimerSetting(
        fontSize: Int? = null,
        xPos: Int? = null,
        yPos: Int? = null,
        firstInterval: Int? = null,
        secondInterval: Int? = null,
    ) {
        viewModelScope.launch {
            manageTimerSettingUsecase.updateTimerSetting(
                timerSetting = ManageTimerSettingUsecase.TimerSetting(
                    fontSize = fontSize,
                    xPos = xPos,
                    yPos = yPos,
                    interval = ManageTimerSettingUsecase.TimerInterval(
                        firstInterval = firstInterval,
                        secondInterval = secondInterval,
                    )
                ),
            )
        }
    }
}