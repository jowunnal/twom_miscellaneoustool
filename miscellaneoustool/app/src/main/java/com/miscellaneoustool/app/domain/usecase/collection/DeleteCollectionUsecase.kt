package com.miscellaneoustool.app.domain.usecase.collection

import com.miscellaneoustool.app.domain.repository.CollectionRepository
import javax.inject.Inject

class DeleteCollectionUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(collectionList: List<Int>) {
        collectionRepository.deleteCollection(collectionList)
    }
}