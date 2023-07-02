package com.jinproject.domain.usecase.collection

import com.jinproject.domain.repository.CollectionRepository
import javax.inject.Inject

class DeleteCollectionUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(collectionList: List<Int>) {
        collectionRepository.deleteCollection(collectionList)
    }
}