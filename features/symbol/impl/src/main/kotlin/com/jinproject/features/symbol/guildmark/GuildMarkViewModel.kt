package com.jinproject.features.symbol.guildmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.design_compose.component.layout.DownLoadedUiState
import com.jinproject.design_compose.component.layout.DownloadableUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = GuildMarkViewModel.Factory::class)
internal class GuildMarkViewModel @AssistedInject constructor(
    @Assisted private val imgUri: String,
) : ViewModel() {
    val uiState: StateFlow<DownloadableUiState> = flow {
        emit(
            GuildMarkUiState(
                imgUri
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DownloadableUiState.Loading,
    )

    @AssistedFactory
    interface Factory {
        fun create(imgUri: String): GuildMarkViewModel
    }
}

internal data class GuildMarkUiState(override val data: String) : DownLoadedUiState<String>()