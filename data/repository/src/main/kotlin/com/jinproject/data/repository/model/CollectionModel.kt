package com.jinproject.data.repository.model

import com.jinproject.domain.entity.ItemCollection

data class CollectionModel(
    val bookId: Int,
    val stat: Map<String, Float>,
    val items: List<GetItemDomainFactory>
) {
    fun toCollectionDomain(): ItemCollection = ItemCollection(
        id = bookId,
        stats = stat,
        requiredItems = items.map {
            it.getDomainFactory().create()
        }
    )
}
