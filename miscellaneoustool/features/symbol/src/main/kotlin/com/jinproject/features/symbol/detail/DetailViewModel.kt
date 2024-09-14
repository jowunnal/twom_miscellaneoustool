package com.jinproject.features.symbol.detail

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.features.symbol.toParsedUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
open class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    val imageDetailState get() = flow<Uri> {
        emit(savedStateHandle.get<String>("imgUri")?.toParsedUri() ?: Uri.EMPTY)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Uri.EMPTY
    )
}