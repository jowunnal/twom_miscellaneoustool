package com.miscellaneoustool.app.domain.usecase

import com.miscellaneoustool.app.domain.model.Category
import com.miscellaneoustool.app.domain.model.CollectionModel
import com.miscellaneoustool.app.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(category: Category): Flow<List<CollectionModel>> =
        collectionRepository.getCollectionList(category)

}