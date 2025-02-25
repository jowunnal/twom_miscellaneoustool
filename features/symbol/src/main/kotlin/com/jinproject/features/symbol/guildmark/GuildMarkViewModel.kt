package com.jinproject.features.symbol.guildmark

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jinproject.design_compose.component.layout.DownLoadedUiState
import com.jinproject.design_compose.component.layout.DownloadableUiState
import com.jinproject.features.symbol.SymbolRoute
import com.jinproject.features.symbol.toParsedUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class GuildMarkViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val uiState: StateFlow<DownloadableUiState> = flow {
        emit(
            GuildMarkUiState(
                savedStateHandle.toRoute<SymbolRoute.GuildMark>().imgUri.toParsedUri()
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DownloadableUiState.Loading,
    )
}

internal data class GuildMarkUiState(override val data: String) : DownLoadedUiState<String>()