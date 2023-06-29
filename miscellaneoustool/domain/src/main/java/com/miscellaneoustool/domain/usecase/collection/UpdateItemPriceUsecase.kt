package com.miscellaneoustool.domain.usecase.collection

import com.miscellaneoustool.domain.repository.CollectionRepository
import javax.inject.Inject

class UpdateItemPriceUsecase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(name: String, price: Int) {
        collectionRepository.updateItemPrice(name = name, price = price)
    }
}