package com.jinproject.features.symbol.preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.repository.CollectionRepository
import com.jinproject.features.symbol.detail.DetailViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: CollectionRepository,
): DetailViewModel(savedStateHandle) {

    fun setPaidImageUri() {
        viewModelScope.launch {
            repository.setSymbolUri(imageDetailState.value)
        }
    }
}
