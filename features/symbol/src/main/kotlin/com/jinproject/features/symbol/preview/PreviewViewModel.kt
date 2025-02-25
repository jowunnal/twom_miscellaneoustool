package com.jinproject.features.symbol.preview

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jinproject.domain.repository.SymbolRepository
import com.jinproject.features.core.AuthManager
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.symbol.StorageManager
import com.jinproject.features.symbol.SymbolRoute
import com.jinproject.features.symbol.toParsedUri
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel
internal class PreviewViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val repository: SymbolRepository,
) : ViewModel() {

    val imageDetailState = flow {
        emit(savedStateHandle.toRoute<SymbolRoute.GuildMarkPreview>().imgUri.toParsedUri())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    @OptIn(ExperimentalUuidApi::class)
    fun addPaidSymbol(navigateToGuildMark: () -> Unit, showSnackBar: (SnackBarMessage) -> Unit) {
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    launch {
                        if (AuthManager.isActive)
                            StorageManager.uploadImage(
                                uri = imageDetailState.value,
                                fileName = Uuid.random().toString(),
                            )
                    }
                    repository.addPaidSymbol(imageDetailState.value)
                }
            }.onFailure { t ->
                showSnackBar(
                    SnackBarMessage(
                        headerMessage = context.getString(com.jinproject.design_ui.R.string.message_throw_exceptions),
                        contentMessage = "${t.message}",
                    )
                )
            }

            navigateToGuildMark()
        }
    }
}
