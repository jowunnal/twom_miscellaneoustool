package com.jinproject.domain.usecase.collection

import com.jinproject.domain.model.Category
import com.jinproject.domain.model.CollectionModel
import com.jinproject.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(category: Category, filter: Boolean): Flow<List<CollectionModel>> =
        collectionRepository.getCollectionList(category, filter)

}