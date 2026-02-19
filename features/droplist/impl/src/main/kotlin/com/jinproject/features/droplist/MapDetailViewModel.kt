package com.jinproject.features.droplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.usecase.droplist.GetMonsterListCompleteByMapUseCase
import com.jinproject.features.droplist.mapper.toMonsterState
import com.jinproject.features.droplist.state.MonsterState
import com.jinproject.features.droplist.state.toMapState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = MapDetailViewModel.Factory::class)
internal class MapDetailViewModel @AssistedInject constructor(
    @Assisted private val mapName: String,
    getMonsterListCompleteByMapUseCase: GetMonsterListCompleteByMapUseCase,
) : ViewModel() {

    val monsterList: StateFlow<ImmutableList<MonsterState>> =
        getMonsterListCompleteByMapUseCase().map { monsterCompletes ->
            monsterCompletes.entries
                .find { (map, _) -> map.name.equals(mapName, true) }
                ?.value
                ?.sortedWith { o1, o2 ->
                    when (val priorityCompared =
                        o1.type.getPriority().compareTo(o2.type.getPriority())) {
                        0 -> when (val levelCompared = o1.level.compareTo(o2.level)) {
                            0 -> o1.name.compareTo(o2.name)
                            else -> levelCompared
                        }

                        else -> priorityCompared
                    }
                }
                ?.map { it.toMonsterState() }
                ?.toImmutableList()
                ?: persistentListOf()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = persistentListOf()
        )

    @AssistedFactory
    interface Factory {
        fun create(mapName: String): MapDetailViewModel
    }
}
