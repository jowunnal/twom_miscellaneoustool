package com.jinproject.features.symbol.gallery

import android.os.Parcelable
import android.provider.MediaStore
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(): ViewModel() {
    private val _images: MutableStateFlow<MTImageList> = MutableStateFlow(MTImageList.getInitValues())
    val images: StateFlow<MTImageList> get() = _images.asStateFlow()


    fun getMoreImages(images: List<MTImage>,) {
        _images.update { state ->
            state.copy(images = state.images.toMutableList().apply { addAll(images) })
        }
    }
}

@Stable
data class MTImageList(
    val images: List<MTImage>,
) {
    companion object {
        fun getInitValues() = MTImageList(
            images = emptyList()
        )
    }
}

@Stable
@Parcelize
data class MTImage(
    val id: Long,
    val uri: String,
): Parcelable {
    companion object {
        const val IMAGE_COLUMNS_ID = MediaStore.Images.ImageColumns._ID
        const val IMAGE_COLUMNS_DATE_ADDED = MediaStore.Images.ImageColumns.DATE_ADDED

        fun getInitValues() = MTImage(
            id = -1L,
            uri = ""
        )
    }
}