package com.jinproject.features.symbol.gallery

import android.os.Parcelable
import android.provider.MediaStore
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: CollectionRepository,
) : ViewModel() {
    private val _images: MutableStateFlow<MTImageList> =
        MutableStateFlow(MTImageList.getInitValues())
    val images: StateFlow<MTImageList> get() = _images.asStateFlow()

    private val _isPaidImage: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPaidImage: StateFlow<Boolean> get() = _isPaidImage.asStateFlow()

    fun getMoreImages(images: List<MTImage>) {
        _images.update { state ->
            state.copy(images = state.images.toMutableList().apply { addAll(images) })
        }
    }

    fun setClickedImage(id: Long) {
        _images.update { state ->
            state.copy(clickedId = id)
        }
    }

    init {
        getStoredSymbolUri()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getStoredSymbolUri() =
        repository.getSymbolUri()
            .flatMapLatest { uris ->
                images.filter { mtImageList ->
                    mtImageList.images.isNotEmpty() && mtImageList.clickedId != -1L
                }.map { mtImageList ->
                    mtImageList.images.find { it.id == images.value.clickedId }?.uri
                }.map { uri ->
                    uri in uris
                }
            }.onEach { bool ->
                _isPaidImage.update { bool }
            }.launchIn(viewModelScope)
}

@Stable
data class MTImageList(
    val images: List<MTImage>,
    val clickedId: Long,
) {
    companion object {
        fun getInitValues() = MTImageList(
            images = emptyList(),
            clickedId = -1L,
        )
    }
}

@Stable
@Parcelize
data class MTImage(
    val id: Long,
    val uri: String,
) : Parcelable {
    companion object {
        const val IMAGE_COLUMNS_ID = MediaStore.Images.ImageColumns._ID
        const val IMAGE_COLUMNS_DATE_ADDED = MediaStore.Images.ImageColumns.DATE_ADDED

        fun getInitValues() = MTImage(
            id = -1L,
            uri = ""
        )
    }
}