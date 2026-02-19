package com.jinproject.features.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.repository.CollectionRepository
import com.jinproject.features.collection.model.Item
import com.jinproject.features.collection.model.ItemCollection
import com.jinproject.features.core.utils.mapToImmutableList
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CollectionDetailViewModel.Factory::class)
internal class CollectionDetailViewModel @AssistedInject constructor(
    private val collectionRepository: CollectionRepository,
    @Assisted private val collectionId: Int,
) : ViewModel() {

    private val _sideEffect = Channel<CollectionDetailSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    val collection: StateFlow<ItemCollection?> =
        collectionRepository.getCollectionList().map { collectionModels ->
            collectionModels.find { it.id == collectionId }?.let { collectionModel ->
                ItemCollection(
                    id = collectionModel.id,
                    stats = collectionModel.stats.toImmutableMap(),
                    items = Item.fromDomainItem(collectionModel.requiredItems),
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun addFilteringCollectionId(id: Int) {
        viewModelScope.launch {
            collectionRepository.addFilteringCollection(id)
            _sideEffect.send(CollectionDetailSideEffect.FilterRemoved)
        }
    }

    fun updateItemsPrice(items: List<Item>) {
        viewModelScope.launch {
            items.onEach { item ->
                collectionRepository.updateItemPrice(name = item.name, price = item.price)
            }
            _sideEffect.send(CollectionDetailSideEffect.PriceUpdated)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(collectionId: Int): CollectionDetailViewModel
    }
}

internal sealed interface CollectionDetailSideEffect {
    data object FilterRemoved : CollectionDetailSideEffect
    data object PriceUpdated : CollectionDetailSideEffect
}
