package com.jinproject.features.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.repository.CollectionRepository
import com.jinproject.domain.repository.DropListRepository
import com.jinproject.domain.repository.TimerRepository
import com.jinproject.features.core.utils.mapToImmutableList
import com.jinproject.features.home.model.Item
import com.jinproject.features.home.model.ItemCollection
import com.jinproject.features.home.model.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import java.time.ZonedDateTime
import javax.inject.Inject

data class HomeUiState(
    val maps: ImmutableList<MapState>,
    val collections: ImmutableList<ItemCollection>,
    val bossTimer: ImmutableList<BossTimer>,
)

@Stable
data class BossTimer(
    val name: String,
    val time: ZonedDateTime,
)

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    collectionRepository: CollectionRepository,
    dropListRepository: DropListRepository,
    timerRepository: TimerRepository,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> =
        collectionRepository.getCollectionList()
            .zip(collectionRepository.getFilteredCollectionIds()) { collectionModels, filteredIds ->
                collectionModels.filter {
                    it.id !in filteredIds
                }.mapToImmutableList { collectionModel ->
                    ItemCollection(
                        id = collectionModel.id,
                        stats = collectionModel.stats.toImmutableMap(),
                        items = Item.fromDomainItem(collectionModel.requiredItems),
                    )
                }
            }.flatMapLatest { itemCollections ->
                dropListRepository.getMapList()
                    .combine(timerRepository.getTimerList()) { mapModelList, timerModels ->
                        HomeUiState(
                            maps = mapModelList.map { mapModel ->
                                MapState(
                                    name = mapModel.name,
                                    imgName = mapModel.imageName,
                                )
                            }.toImmutableList(),
                            collections = itemCollections,
                            bossTimer = timerModels.map { timerModel ->
                                BossTimer(
                                    name = timerModel.monsterName,
                                    time = timerModel.dateTime,
                                )
                            }.toImmutableList(),
                        )
                    }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState(
                    persistentListOf(),
                    persistentListOf(),
                    persistentListOf()
                )
            )
}