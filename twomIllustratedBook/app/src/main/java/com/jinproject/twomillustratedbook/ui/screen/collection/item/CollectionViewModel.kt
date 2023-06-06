package com.jinproject.twomillustratedbook.ui.screen.collection.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.twomillustratedbook.domain.repository.CollectionRepository
import com.jinproject.twomillustratedbook.ui.screen.collection.item.item.CollectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
data class CollectionUiState(
    val collectionList: List<CollectionState>
)  {
    companion object {
        fun getInitValue() = CollectionUiState(
            collectionList = emptyList()
        )
    }
}
@HiltViewModel
class CollectionViewModel @Inject constructor(private val collectionRepositoryImpl: CollectionRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionUiState.getInitValue())
    val uiState get() = _uiState.asStateFlow()

    fun getCollectionList(category: String) = collectionRepositoryImpl.getCollectionList(category).onEach { collectionModelList ->
        _uiState.update { state ->
            state.copy(collectionList = collectionModelList.map { collectionModel -> CollectionState.fromCollectionModel(collectionModel) })
        }
    }.catch {

    }.launchIn(viewModelScope)

}