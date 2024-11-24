package com.jinproject.features.symbol.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.repository.SymbolRepository
import com.jinproject.features.core.utils.mapToImmutableList
import com.jinproject.features.symbol.ai.DownloadState.Companion.DOWNLOADING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
internal class GenerateImageViewModel @Inject constructor(
    private val symbolRepository: SymbolRepository,
) : ViewModel() {

    val uiState: StateFlow<GenerateImageUiState> =
        symbolRepository.getChatList().map { chatMessages ->
            GenerateImageUiState(
                messages = chatMessages.mapToImmutableList { chatMessage ->
                    Message(
                        publisher = Publisher.valueOf(chatMessage.publisher),
                        data = chatMessage.data,
                        timeStamp = chatMessage.timeStamp,
                    )
                }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GenerateImageUiState.getInitValues()
        )

    fun generateImage(prompt: String) {
        viewModelScope.launch(Dispatchers.IO) {
            symbolRepository.generateSymbolImage(prompt)
        }
    }

    fun downloadImage(message: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            symbolRepository.replaceChat(message.copy(data = DOWNLOADING).toDomainMessage())
            symbolRepository.downloadImage(
                url = message.data,
                timeStamp = message.timeStamp,
            )
        }.invokeOnCompletion { cause ->
            viewModelScope.launch(Dispatchers.IO) {
                if (cause is CancellationException)
                    symbolRepository.replaceChat(
                        message.toDomainMessage()
                    )
            }
        }

    }

}

internal data class GenerateImageUiState(
    val messages: ImmutableList<Message>,
) {
    companion object {
        fun getInitValues() = GenerateImageUiState(
            messages = persistentListOf()
        )
    }
}

internal data class Message(
    val publisher: Publisher,
    val data: String,
    val timeStamp: Long,
    val downloadState: DownloadState = DownloadState.getDownloadState(data),
) {
    fun isWebImage() = data.startsWith("http")
    fun toDomainMessage() = com.jinproject.domain.model.Message(
        publisher = publisher.name,
        data = data,
        timeStamp = timeStamp,
    )

}

internal enum class Publisher {
    User,
    AI,
}

internal sealed interface DownloadState {
    data object NotDownloaded : DownloadState
    data object Downloading : DownloadState
    data object Downloaded : DownloadState

    companion object {
        const val DOWNLOADING = "downloading"

        fun getDownloadState(uri: String): DownloadState = when {
            uri.startsWith("http") || uri.isBlank() -> NotDownloaded
            uri.startsWith(DOWNLOADING) -> Downloading
            else ->  Downloaded
        }
    }
}