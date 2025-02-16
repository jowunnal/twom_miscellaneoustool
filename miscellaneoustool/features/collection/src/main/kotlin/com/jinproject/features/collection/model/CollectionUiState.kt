package com.jinproject.features.collection.model

import com.jinproject.core.util.runIf
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class CollectionUiState(
    val itemCollections: ImmutableList<ItemCollection>,
    val collectionFilters: ImmutableList<CollectionFilter>,
) {
    private fun filter(isFilterMode: Boolean): ImmutableList<ItemCollection> {
        val filteredIds = collectionFilters.map { it.id }

        return if (isFilterMode)
            itemCollections
        else
            itemCollections.filter { itemCollection ->
                itemCollection.id !in filteredIds
            }.toImmutableList()
    }

    fun filterBySearchWord(s: String, isFilterMode: Boolean): ImmutableList<ItemCollection> {
        return filter(isFilterMode).runIf(s.isNotBlank()) {
            filter { collection ->
                collection.items.firstNotNullOfOrNull { item ->
                    item.name.contains(s, true)
                } == true ||
                        collection.stats.firstNotNullOfOrNull { stat ->
                            stat.key.contains(s, true)
                        } == true
            }.toImmutableList()
        }
    }
}

data class CollectionFilter(
    val id: Int,
    val isSelected: Boolean = false,
)