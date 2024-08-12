package com.jinproject.domain.usecase.collection

import com.jinproject.domain.model.ItemModel
import com.jinproject.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(): Flow<List<ItemModel>> = collectionRepository.getItems()
}