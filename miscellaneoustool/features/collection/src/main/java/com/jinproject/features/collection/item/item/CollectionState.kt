package com.jinproject.features.collection.item.item

import android.content.Context
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.features.collection.mapper.toCollectionItemState

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
        fun fromCollectionModel(collectionModel: com.jinproject.domain.model.CollectionModel, context:Context) = CollectionState(
            id =  collectionModel.bookId,
            stats = mutableListOf<StatState>().apply {
                collectionModel.stat.forEach { statItem ->
                    add(
                        StatState(
                        value = statItem.value,
                        name = context.doOnLocaleLanguage(
                            onKo = statItem.key.displayName,
                            onElse = statItem.key.displayOtherLanguage
                        )
                    )
                    )
                }
            },
            items = collectionModel.items.map { item -> item.toCollectionItemState() }
        )
    }
}
