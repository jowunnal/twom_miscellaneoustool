package com.jinproject.features.collection.setting.filter

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.features.collection.item.CollectionUiState
import com.jinproject.features.collection.item.item.CollectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionSettingFilterViewModel @Inject constructor(
    private val getCollectionUsecase: com.jinproject.domain.usecase.collection.GetCollectionUsecase,
    private val deleteCollectionUsecase: com.jinproject.domain.usecase.collection.DeleteFilterUsecase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionUiState.getInitValue())
    val uiState get() = _uiState.asStateFlow()

    fun getCollectionList(category: com.jinproject.domain.model.Category) =
        getCollectionUsecase(category, false)
            .onEach { collectionModelList ->
                _uiState.update { state ->
                    state.copy(collectionList = collectionModelList.map { collectionModel ->
                        CollectionState.fromCollectionModel(collectionModel, context)
                    })
                }
            }.catch { e ->
                Log.d("test", "${e.message}")
            }.launchIn(viewModelScope)

    fun deleteFilter(id: Int, category: com.jinproject.domain.model.Category) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteCollectionUsecase.invoke(id)
            getCollectionList(category)
        }
    }

}