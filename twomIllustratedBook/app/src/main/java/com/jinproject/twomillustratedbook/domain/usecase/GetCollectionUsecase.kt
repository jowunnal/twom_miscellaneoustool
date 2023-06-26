package com.jinproject.twomillustratedbook.domain.usecase

import com.jinproject.twomillustratedbook.domain.model.Category
import com.jinproject.twomillustratedbook.domain.model.CollectionModel
import com.jinproject.twomillustratedbook.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(category: Category): Flow<List<CollectionModel>> =
        collectionRepository.getCollectionList(category)

}