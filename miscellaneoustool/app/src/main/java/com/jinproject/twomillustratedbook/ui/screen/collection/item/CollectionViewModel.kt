package com.jinproject.twomillustratedbook.ui.screen.collection.item

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.twomillustratedbook.ui.screen.collection.item.item.CollectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CollectionUiState(
    val collectionList: List<CollectionState>
) {
    companion object {
        fun getInitValue() = CollectionUiState(
            collectionList = emptyList()
        )
    }
}

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val getCollectionUsecase: com.jinproject.domain.usecase.collection.GetCollectionUsecase,
    private val deleteCollectionUsecase: com.jinproject.domain.usecase.collection.DeleteCollectionUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionUiState.getInitValue())
    val uiState get() = _uiState.asStateFlow()

    fun getCollectionList(category: com.jinproject.domain.model.Category) =
        getCollectionUsecase(category, true)
            .onEach { collectionModelList ->
                _uiState.update { state ->
                    state.copy(collectionList = collectionModelList.map { collectionModel ->
                        CollectionState.fromCollectionModel(collectionModel)
                    })
                }
            }.catch { e ->
                Log.d("test", "${e.message}")
            }.launchIn(viewModelScope)

    fun deleteCollection(collectionList: List<Int>, category: com.jinproject.domain.model.Category) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteCollectionUsecase.invoke(collectionList)
            getCollectionList(category)
        }
    }

}