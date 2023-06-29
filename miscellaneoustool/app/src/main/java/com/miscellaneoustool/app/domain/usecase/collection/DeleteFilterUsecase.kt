package com.miscellaneoustool.app.domain.usecase.collection

import com.miscellaneoustool.app.domain.repository.CollectionRepository
import javax.inject.Inject

class DeleteFilterUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(id: Int) {
        collectionRepository.deleteFilter(id)
    }
}