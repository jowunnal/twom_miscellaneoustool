package com.miscellaneoustool.app.ui.screen.collection.item.item

import com.miscellaneoustool.app.domain.model.CollectionModel

data class CollectionState(
    val id: Int,
    val stats: List<StatState>,
    val items: List<CollectionItemState>,
    var isCheck: CheckState = CheckState.INVISIBLE
) {
    enum class CheckState {
        INVISIBLE,
        CHECKED,
        UNCHECKED
    }

    companion object {
        fun fromCollectionModel(collectionModel: CollectionModel) = CollectionState(
            id =  collectionModel.bookId,
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
