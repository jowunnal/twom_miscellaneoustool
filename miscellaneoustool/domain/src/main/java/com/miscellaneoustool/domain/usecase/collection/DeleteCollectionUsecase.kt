package com.miscellaneoustool.domain.usecase.collection

import com.miscellaneoustool.domain.repository.CollectionRepository
import javax.inject.Inject

class DeleteCollectionUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(collectionList: List<Int>) {
        collectionRepository.deleteCollection(collectionList)
    }
}