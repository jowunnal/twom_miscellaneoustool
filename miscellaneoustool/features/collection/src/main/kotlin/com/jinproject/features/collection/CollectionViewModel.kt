package com.jinproject.features.collection

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.domain.model.ItemType
import com.jinproject.domain.repository.CollectionRepository
import com.jinproject.features.collection.model.CollectionFilter
import com.jinproject.features.collection.model.CollectionUiState
import com.jinproject.features.collection.model.Equipment
import com.jinproject.features.collection.model.Item
import com.jinproject.features.collection.model.Item.Companion.toItemModel
import com.jinproject.features.collection.model.ItemCollection
import com.jinproject.features.collection.model.MiscellaneousItem
import com.jinproject.features.core.utils.mapToImmutableList
import com.jinproject.features.core.utils.putAllToImmutableMap
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CollectionViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _filteringCollectionIds: MutableStateFlow<ImmutableSet<Int>> =
        MutableStateFlow<ImmutableSet<Int>>(persistentSetOf())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val filteringCollectionIds: StateFlow<ImmutableSet<Int>> =
        collectionRepository.getFilteredCollectionIds().flatMapLatest { ids ->
            _filteringCollectionIds.apply {
                if (value.isEmpty())
                    update { ids.toImmutableSet() }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = persistentSetOf()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CollectionUiState> =
        collectionRepository.getCollectionList().map { collectionModels ->
            collectionModels.mapToImmutableList { collectionModel ->
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
        }.flatMapLatest { collectionModels ->
            filteringCollectionIds.map { filteringList ->
                CollectionUiState(
                    itemCollections = collectionModels,
                    collectionFilters = filteringList.map { id ->
                        CollectionFilter(id = id, true)
                    }.toImmutableList()
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CollectionUiState(persistentListOf(), persistentListOf())
        )

    fun dispatchCollectionEvent(event: CollectionEvent) {
        when(event) {
            is CollectionEvent.AddFilteringCollectionId -> addFilteringCollectionId(event.id)
            CollectionEvent.SetFilteredCollection -> setFilteredCollection()
            is CollectionEvent.UpdateItemsPrice -> updateItemsPrice(event.items)
        }
    }

    private fun addFilteringCollectionId(id: Int) {
        _filteringCollectionIds.update { set ->
            set.toMutableSet().apply {
                if (!set.contains(id))
                    add(id)
                else
                    remove(id)
            }.toImmutableSet()
        }
    }

    private fun setFilteredCollection() {
        viewModelScope.launch {
            collectionRepository.setFilteringCollections(filteringCollectionIds.value)
        }
    }

    private fun updateItemsPrice(items: List<Item>) {
        viewModelScope.launch {
            collectionRepository.updateItemPrice(items.map { it.toItemModel() })
        }
    }
}

internal sealed interface CollectionEvent {
    data class AddFilteringCollectionId(val id: Int): CollectionEvent
    data object SetFilteredCollection: CollectionEvent
    data class UpdateItemsPrice(val items: List<Item>): CollectionEvent
}