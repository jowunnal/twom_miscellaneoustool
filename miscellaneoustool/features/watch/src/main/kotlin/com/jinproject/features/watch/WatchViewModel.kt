package com.jinproject.features.watch

import android.app.NotificationManager
import android.content.Context
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.design_compose.component.ButtonStatus
import com.jinproject.domain.usecase.timer.GetOverlaySettingUsecase
import com.jinproject.domain.usecase.timer.SetOverlaySettingUsecase
import com.jinproject.features.alarm.item.TimerState
import com.jinproject.features.alarm.mapper.toTimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
        fun getInitValue(context: Context) = WatchUiState(
            frequentlyUsedBossList = emptyList(),
            selectedMonsterName = "",
            timerList = emptyList(),
            watchStatus = checkOverlayServiceState(context),
            fontSize = 14,
            xPos = 0,
            yPos = 0
        )

        private fun checkOverlayServiceState(context: Context): ButtonStatus {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            return notificationManager.activeNotifications.find { it.id == 999 }?.let {
                ButtonStatus.ON
            } ?: ButtonStatus.OFF
        }
    }
}

@HiltViewModel
class WatchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val timerRepository: com.jinproject.domain.repository.TimerRepository,
    private val setOverlaySettingUsecase: SetOverlaySettingUsecase,
    private val getOverlaySettingUsecase: GetOverlaySettingUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchUiState.getInitValue(context))
    val uiState get() = _uiState.asStateFlow()

    init {
        getRecentlySelectedBossInfo()
        getTimerList()
    }

    private fun getRecentlySelectedBossInfo() {
        getOverlaySettingUsecase().onEach { overlaySetting ->
            _uiState.update { state ->
                state.copy(
                    frequentlyUsedBossList = overlaySetting.frequentlyUsedBossList,
                    fontSize = overlaySetting.fontSize,
                    xPos = overlaySetting.xPos,
                    yPos = overlaySetting.yPos
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