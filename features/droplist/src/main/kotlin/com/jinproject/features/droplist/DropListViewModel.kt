package com.jinproject.features.droplist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jinproject.features.droplist.mapper.toMonsterState
import com.jinproject.features.droplist.state.MapState
import com.jinproject.features.droplist.state.MonsterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DropListUiState(
    val maps: ImmutableList<MapState>,
    val monsters: ImmutableList<MonsterState>,
    val selectedMap: MapState?,
) {
    companion object {
        fun getInitValue() = DropListUiState(
            maps = persistentListOf(),
            monsters = persistentListOf(),
            selectedMap = null,
        )
    }
}

@HiltViewModel
class DropListViewModel @Inject constructor(
    dropListRepository: com.jinproject.domain.repository.DropListRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _selectedMap: MutableStateFlow<String?> =
        MutableStateFlow(savedStateHandle.toRoute<DropListRoute.MapList>().mapName)

    fun selectMap(map: MapState) = _selectedMap.update { map.name }

    @OptIn(ExperimentalCoroutinesApi::class)
    val dropListUiState: StateFlow<DropListUiState> =
        dropListRepository.getMaps().map { mapModelList ->
            mapModelList.map { mapModel ->
                MapState(
                    name = mapModel.name,
                    imgName = mapModel.imageName,
                )
            }.toImmutableList()
        }.flatMapLatest { maps ->
            _selectedMap.mapLatest { mapName ->
                val monsters = mapName?.let {
                    dropListRepository.getMonsterListFromMap(mapName).map { monsterModelList ->
                        monsterModelList.map { monsterModel ->
                            monsterModel.toMonsterState()
                        }.sorted().toImmutableList()
                    }.first()
                } ?: persistentListOf()

                val selectedMap = maps.find { it.name == mapName }

                DropListUiState(
                    maps = maps,
                    monsters = monsters,
                    selectedMap = selectedMap,
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DropListUiState.getInitValue(),
        )

}