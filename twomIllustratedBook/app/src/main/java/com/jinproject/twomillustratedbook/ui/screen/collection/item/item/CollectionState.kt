package com.jinproject.twomillustratedbook.ui.screen.collection.item.item

import com.jinproject.twomillustratedbook.domain.model.CollectionModel

data class CollectionState(
    val stats: List<StatState>,
    val items: List<CollectionItemState>
) {
    companion object {
        fun fromCollectionModel(collectionModel: CollectionModel) = CollectionState(
            stats = mutableListOf<StatState>().apply {
                collectionModel.stat.forEach { statItem ->
                    add(StatState(
                        value = statItem.value,
                        name = statItem.key.displayName
                    ))
                }
            },
            items = collectionModel.items.map { item -> item.toCollectionItemState() }
        )
    }
}
