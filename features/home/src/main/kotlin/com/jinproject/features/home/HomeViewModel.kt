package com.jinproject.features.home

import android.content.Context
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.domain.model.ItemType
import com.jinproject.domain.repository.CollectionRepository
import com.jinproject.domain.repository.DropListRepository
import com.jinproject.domain.repository.TimerRepository
import com.jinproject.features.collection.model.Equipment
import com.jinproject.features.collection.model.ItemCollection
import com.jinproject.features.collection.model.MiscellaneousItem
import com.jinproject.features.core.utils.mapToImmutableList
import com.jinproject.features.core.utils.putAllToImmutableMap
import com.jinproject.features.droplist.state.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import java.time.ZoneId
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
    @ApplicationContext context: Context,
    collectionRepository: CollectionRepository,
    dropListRepository: DropListRepository,
    timerRepository: TimerRepository,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> =
        collectionRepository.getCollectionList()
            .zip(collectionRepository.getFilteredCollectionIds()) { collectionModels, filteredIds ->
                collectionModels.filter {
                    it.bookId !in filteredIds
                }.mapToImmutableList { collectionModel ->
                    ItemCollection(
                        id = collectionModel.bookId,
                        stats = collectionModel.stat.putAllToImmutableMap { entry ->
                            context.doOnLocaleLanguage(
                                onKo = entry.key.displayName,
                                onElse = entry.key.displayOtherLanguage
                            ) to entry.value.toFloat()
                        },
                        items = collectionModel.items.mapToImmutableList { item ->
                            when (item.type) {
                                is ItemType.Weapon, is ItemType.Armor -> Equipment(
                                    name = item.name,
                                    count = item.count,
                                    enchantNumber = item.enchantNumber,
                                    price = item.price,
                                )

                                is ItemType.Miscellaneous, is ItemType.Accessory, is ItemType.Costume -> MiscellaneousItem(
                                    name = item.name,
                                    count = item.count,
                                    price = item.price,
                                )

                                else -> throw IllegalStateException("[${item.type}] 은 아이템 도감에서 허용되지 않는 타입의 아이템 입니다.")
                            }
                        },
                    )
                }
            }.flatMapLatest { itemCollections ->
            dropListRepository.getMaps()
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
            initialValue = HomeUiState(persistentListOf(), persistentListOf(), persistentListOf())
        )
}