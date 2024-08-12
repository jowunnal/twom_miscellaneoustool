package com.jinproject.domain.usecase.collection

import com.jinproject.domain.repository.CollectionRepository
import javax.inject.Inject

class DeleteFilterUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(id: Int) {
        collectionRepository.deleteFilter(id)
    }
}