package com.jinproject.twomillustratedbook.ui.screen.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.twomillustratedbook.domain.Item.TimerItem
import com.jinproject.twomillustratedbook.data.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(private val timerRepositoryImpl: TimerRepository) :
    ViewModel() {
    fun setTimer(timerItem: TimerItem) = viewModelScope.launch(Dispatchers.IO) {
        timerRepositoryImpl.setTimer(
            timerItem.day,
            timerItem.hour,
            timerItem.min,
            timerItem.sec,
            timerItem.name
        )
    }

    fun deleteTimer(name: String) =
        viewModelScope.launch(Dispatchers.IO) { timerRepositoryImpl.deleteTimer(name) }

    val timer = timerRepositoryImpl.getTimer()
    fun setOta(ota: Int, name: String) =
        viewModelScope.launch(Dispatchers.IO) { timerRepositoryImpl.setOta(ota, name) }
}