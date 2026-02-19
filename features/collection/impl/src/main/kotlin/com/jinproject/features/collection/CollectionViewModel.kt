package com.jinproject.features.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.repository.CollectionRepository
import com.jinproject.features.collection.model.CollectionUiState
import com.jinproject.features.collection.model.Item
import com.jinproject.features.collection.model.ItemCollection
import com.jinproject.features.core.utils.mapToImmutableList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CollectionViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CollectionUiState> =
        collectionRepository.getCollectionList().map { collectionModels ->
            collectionModels.mapToImmutableList { collectionModel ->
                ItemCollection(
                    id = collectionModel.id,
                    stats = collectionModel.stats.toImmutableMap(),
                    items = Item.fromDomainItem(collectionModel.requiredItems),
                )
            }
        }.flatMapLatest { collectionModels ->
            collectionRepository.getFilteredCollectionIds().map { filteringList ->
                CollectionUiState(
                    itemCollections = collectionModels,
                    collectionFilters = filteringList.toImmutableSet(),
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CollectionUiState(persistentListOf(), persistentSetOf())
        )

    fun dispatchCollectionEvent(event: CollectionEvent) {
        when (event) {
            is CollectionEvent.AddFilteringCollectionId -> addFilteringCollectionId(event.id)
            is CollectionEvent.UpdateItemsPrice -> updateItemsPrice(event.items)
            is CollectionEvent.SetFilteringCollectionIdList -> setFilteringCollectionIdList(event.ids)
        }
    }

    private fun addFilteringCollectionId(id: Int) {
        viewModelScope.launch {
            collectionRepository.addFilteringCollection(id)
        }
    }

    private fun setFilteringCollectionIdList(ids: List<Int>) {
        viewModelScope.launch {
            collectionRepository.setFilteringCollections(ids)
        }
    }

    private fun updateItemsPrice(items: List<Item>) {
        viewModelScope.launch {
            items.onEach { item ->
                collectionRepository.updateItemPrice(name = item.name, price = item.price)
            }
        }
    }
}

internal sealed interface CollectionEvent {
    data class AddFilteringCollectionId(val id: Int) : CollectionEvent
    data class SetFilteringCollectionIdList(val ids: List<Int>) : CollectionEvent
    data class UpdateItemsPrice(val items: List<Item>) : CollectionEvent
}
