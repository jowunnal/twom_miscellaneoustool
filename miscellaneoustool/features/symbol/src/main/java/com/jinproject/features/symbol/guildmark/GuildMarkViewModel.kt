package com.jinproject.features.symbol.guildmark

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jinproject.features.symbol.DetailImageUri
import com.jinproject.features.symbol.toParsedUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GuildMarkViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _imageDetailState: MutableStateFlow<Uri> =
        MutableStateFlow(Uri.EMPTY)
    val imageDetailState get() = _imageDetailState.asStateFlow()

    init {
        getImageDetail()
    }

    private fun getImageDetail() {
        val uri = savedStateHandle.get<String>(DetailImageUri)

        uri?.let {
            _imageDetailState.update { uri.toParsedUri() }
        }
    }
}
