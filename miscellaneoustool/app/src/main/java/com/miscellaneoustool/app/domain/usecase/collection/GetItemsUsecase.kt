package com.miscellaneoustool.app.domain.usecase.collection

import com.miscellaneoustool.app.domain.model.ItemModel
import com.miscellaneoustool.app.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(): Flow<List<ItemModel>> = collectionRepository.getItems()
}