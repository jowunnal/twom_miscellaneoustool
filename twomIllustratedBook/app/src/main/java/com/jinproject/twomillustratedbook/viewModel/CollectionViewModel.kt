package com.jinproject.twomillustratedbook.viewModel

import androidx.lifecycle.ViewModel
import com.jinproject.twomillustratedbook.Repository.CollectionRepository
import com.jinproject.twomillustratedbook.Repository.CollectionRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(private val collectionRepositoryImpl: CollectionRepository) : ViewModel(){
    fun content(data:String) = collectionRepositoryImpl.bookList(data)
}