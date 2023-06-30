package com.miscellaneoustool.domain.usecase.collection

import com.miscellaneoustool.domain.repository.CollectionRepository
import javax.inject.Inject

class DeleteFilterUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(id: Int) {
        collectionRepository.deleteFilter(id)
    }
}