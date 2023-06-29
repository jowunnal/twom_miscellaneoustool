package com.miscellaneoustool.domain.usecase.collection

import com.miscellaneoustool.domain.model.ItemModel
import com.miscellaneoustool.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(): Flow<List<ItemModel>> = collectionRepository.getItems()
}