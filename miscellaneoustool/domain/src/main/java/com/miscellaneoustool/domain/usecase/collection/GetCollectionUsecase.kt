package com.miscellaneoustool.domain.usecase.collection

import com.miscellaneoustool.domain.model.Category
import com.miscellaneoustool.domain.model.CollectionModel
import com.miscellaneoustool.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(category: Category, filter: Boolean): Flow<List<CollectionModel>> =
        collectionRepository.getCollectionList(category, filter)

}