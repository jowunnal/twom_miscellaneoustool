package com.jinproject.features.droplist

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateSet
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jinproject.domain.usecase.droplist.GetMonsterListCompleteByMapUseCase
import com.jinproject.features.droplist.mapper.toMonsterState
import com.jinproject.features.droplist.state.ItemState
import com.jinproject.features.droplist.state.MapState
import com.jinproject.features.droplist.state.MonsterState
import com.jinproject.features.droplist.state.Searchable
import com.jinproject.features.droplist.state.toMapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Stable
data class DropListUiState(
    val monstersGroupedByMap: ImmutableMap<MapState, ImmutableList<MonsterState>>,
) {
    private val allMonsters =
        monstersGroupedByMap.values.flatten().distinctBy { it.name }.toImmutableList()

    private val allSearchables: ImmutableList<Searchable> =
        (allMonsters + allMonsters.flatMap { it.items }.distinctBy { it.name })
            .sorted()
            .toImmutableList()

    private var _searchQuery: String by mutableStateOf("")
    private var _selectedMap: MapState by mutableStateOf<MapState>(MapState.getInitValue())
    private var selectedMonsters: SnapshotStateSet<MonsterState> =
        mutableStateSetOf(MonsterState.getInitValue())

    val searchQuery: String by derivedStateOf { _searchQuery }

    val selectedMap: MapState by derivedStateOf { _selectedMap }

    val searchables: ImmutableList<Searchable> by derivedStateOf {
        if (searchQuery.isBlank())
            persistentListOf()
        else
            allSearchables.filter { it.name.contains(searchQuery, true) }
                .toImmutableList()
    }

    val monsterListExistInSelectedMap: Monsters by derivedStateOf {
        Monsters(monstersGroupedByMap[selectedMap]?.toImmutableList() ?: persistentListOf())
    }

    val mapListWhereSelectedMonsterLive: Maps by derivedStateOf {
        Maps(
            monstersGroupedByMap.filter { entry ->
                entry.value.any { monster -> selectedMonsters.find { it.name == monster.name } != null }
            }.keys.toImmutableList()
        )
    }

    val mapStateList: ImmutableList<MapState> = monstersGroupedByMap.keys.toImmutableList()

    fun updateSearchQuery(searchQuery: String) {
        this._searchQuery = searchQuery
    }

    fun updateSelectedMap(selectedMap: MapState) {
        this._selectedMap = selectedMap
    }

    fun setSelectedMonster(vararg selectedMonsters: MonsterState) {
        this.selectedMonsters.clear()
        this.selectedMonsters.addAll(selectedMonsters)
    }

    fun setSelectedSearchable(searchable: Searchable) {
        when (searchable) {
            is MonsterState -> setSelectedMonster(searchable)
            is ItemState -> {
                val monstersWithItem = allMonsters.filter { monster ->
                    monster.items.any { it.name == searchable.name }
                }
                setSelectedMonster(*monstersWithItem.toTypedArray())
            }
        }
    }

    companion object {
        fun getInitValue() = DropListUiState(
            monstersGroupedByMap = persistentMapOf()
        )
    }
}

@HiltViewModel
class DropListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    getMonsterListCompleteByMapUseCase: GetMonsterListCompleteByMapUseCase,
) : ViewModel() {

    val dropListUiState: StateFlow<DropListUiState> =
        getMonsterListCompleteByMapUseCase().map { monsterCompletes ->
            DropListUiState(
                monstersGroupedByMap = monsterCompletes.entries.associate { (map, monsters) ->
                    map.toMapState() to monsters.sortedWith { o1, o2 ->
                        when (val priorityCompared =
                            o1.type.getPriority().compareTo(o2.type.getPriority())) {
                            0 -> when (val levelCompared = o1.level.compareTo(o2.level)) {
                                0 -> o1.name.compareTo(o2.name)
                                else -> levelCompared
                            }

                            else -> priorityCompared
                        }
                    }.map { it.toMonsterState() }.toImmutableList()
                }.toImmutableMap(),
            ).apply {
                savedStateHandle.toRoute<DropListRoute.MapList>().mapName?.let { mapName ->
                    updateSelectedMap(
                        monsterCompletes.keys.first { map ->
                            map.name.equals(mapName, true)
                        }.toMapState()
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DropListUiState.getInitValue()
        )
}

@Immutable
class Monsters(val monsters: ImmutableList<MonsterState>)

@Immutable
class Maps(val maps: ImmutableList<MapState>)
