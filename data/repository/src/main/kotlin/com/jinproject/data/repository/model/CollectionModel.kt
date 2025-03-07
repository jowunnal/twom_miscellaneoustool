package com.jinproject.data.repository.model

data class CollectionModel(
    val bookId: Int,
    val stat: Map<String, Double>,
    val items: List<ItemModel>
)
