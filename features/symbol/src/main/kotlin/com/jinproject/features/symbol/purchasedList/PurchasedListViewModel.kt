package com.jinproject.features.symbol.purchasedList


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.StorageException
import com.jinproject.design_compose.component.layout.DownLoadedUiState
import com.jinproject.design_compose.component.layout.DownloadableUiState
import com.jinproject.features.core.AuthManager
import com.jinproject.features.core.utils.RestartableStateFlow
import com.jinproject.features.core.utils.restartableStateIn
import com.jinproject.features.symbol.StorageManager
import com.jinproject.features.symbol.gallery.MTImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.tasks.await
import java.time.ZonedDateTime
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

@HiltViewModel
class PurchasedListViewModel @Inject constructor() : ViewModel() {
    private var pageToken: String? = null
    private var id = AtomicLong(0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: RestartableStateFlow<DownloadableUiState> = flow {
        kotlin.runCatching {
            StorageManager.getImageList(pageToken = pageToken).also {
                pageToken = it.pageToken
            }
        }.onSuccess { listResult ->
            emit(
                PurchasedListUiState(
                    listResult.items
                        .asFlow()
                        .flatMapMerge(concurrency = 20) { url ->
                            flow {
                                emit(
                                    MTImage(
                                        id = id.getAndIncrement(),
                                        uri = url.downloadUrl.await().toString(),
                                        modifiedDate = ZonedDateTime.now().toString()
                                    )
                                )
                            }
                        }.toList().toPersistentList()
                )
            )
        }.onFailure { t ->
            if (t is StorageException) {
                if (t.errorCode == StorageException.ERROR_BUCKET_NOT_FOUND) {
                    emit(PurchasedListUiState(persistentListOf()))
                }
            }
        }
    }.restartableStateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DownloadableUiState.Loading,
    )

    fun getNextPage() {
        uiState.stopAndStart()
    }

}

internal data class PurchasedListUiState(
    override val data: PersistentList<MTImage>
) : DownLoadedUiState<PersistentList<MTImage>>()
