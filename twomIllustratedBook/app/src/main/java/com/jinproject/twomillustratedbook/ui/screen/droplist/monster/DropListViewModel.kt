package com.jinproject.twomillustratedbook.ui.screen.droplist.monster

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.twomillustratedbook.domain.repository.DropListRepository
import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.item.ItemState
import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.item.MonsterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class DropListUiState(
    val monster: List<MonsterState>
) {
    companion object {
        fun getInitValue() = DropListUiState(
            monster = emptyList()
        )
    }
}

@HiltViewModel
class DropListViewModel @Inject constructor(
    private val repository: DropListRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DropListUiState.getInitValue())
    val uiState get() = _uiState.asStateFlow()

    fun getMonsterListFromMap(map: String) =
        repository.getMonsterListFromMap(map).onEach { monsterModelList ->
            _uiState.update { state ->
                state.copy(monster = monsterModelList.map { monsterModel ->
                    monsterModel.toMonsterState()
                }.sortedWith(compareBy { monsterState -> monsterState.level }))
            }
        }.catch { e ->
            Log.d("test", "exception : ${e.message}\n ${e.printStackTrace()}")
        }.launchIn(viewModelScope)

    fun itemListToSingleString(itemList: List<ItemState>): String {
        var contents = ""
        itemList.forEachIndexed { index, item ->
            contents += if (itemList.lastIndex != index) "${item.name}, " else item.name
        }
        return contents
    }
}