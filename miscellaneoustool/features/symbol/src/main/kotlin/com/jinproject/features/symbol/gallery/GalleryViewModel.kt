package com.jinproject.features.symbol.gallery

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.domain.repository.SymbolRepository
import com.jinproject.features.symbol.gallery.MTImage.Companion.IMAGE_COLUMNS_ID
import com.jinproject.features.symbol.gallery.MTImage.Companion.IMAGE_COLUMNS_MODIFIED_DATE
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: SymbolRepository,
) : ViewModel() {
    private val _images: MutableStateFlow<MTImageList> =
        MutableStateFlow(MTImageList.getInitValues())
    val images: StateFlow<MTImageList> get() = _images.asStateFlow()

    private val _isPaidImage: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPaidImage: StateFlow<Boolean> get() = _isPaidImage.asStateFlow()

    suspend fun getMoreImages(context: Context) {
        val moreImages = getImageFromContentProvider(
            resolver = context.contentResolver,
            selection = "$IMAGE_COLUMNS_MODIFIED_DATE < ?",
            selectionArgs = arrayOf(
                this.images.value.images.lastOrNull()?.modifiedDate ?: Instant.now().toEpochMilli().toString(),
            ),
        )

        _images.update { state ->
            state.copy(images = state.images.toMutableList().apply { addAll(moreImages) })
        }
    }

    fun setClickedImage(id: Long) {
        _images.update { state ->
            state.copy(clickedId = id)
        }
    }

    init {
        getStoredSymbolUri()

        viewModelScope.launch {
            getMoreImages(context)
        }
    }

    private suspend fun getImageFromContentProvider(
        resolver: ContentResolver,
        imageUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        limit: Int = 100,
    ): MutableList<MTImage> {
        val imageList = mutableListOf<MTImage>()

        val projection = arrayOf(
            IMAGE_COLUMNS_ID,
            IMAGE_COLUMNS_MODIFIED_DATE,
        )

        val bundle = Bundle().apply {
            putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
            putInt(ContentResolver.QUERY_ARG_OFFSET, 1)
            putStringArray(
                ContentResolver.QUERY_ARG_SORT_COLUMNS,
                arrayOf(IMAGE_COLUMNS_MODIFIED_DATE)
            )

            putInt(
                ContentResolver.QUERY_ARG_SORT_DIRECTION,
                ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
            )

            putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
            putStringArray(
                ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                selectionArgs
            )
        }

        val query: Cursor? = withContext(Dispatchers.IO) {
            resolver.query(
                imageUri,
                projection,
                bundle,
                null
            )
        }

        query?.use { cursor ->
            while (cursor.moveToNext()) {
                val idColumn = cursor.getColumnIndexOrThrow(IMAGE_COLUMNS_ID)
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(imageUri, id).toString()

                val modifiedDateColumn = cursor.getColumnIndexOrThrow(IMAGE_COLUMNS_MODIFIED_DATE)
                val modifiedDate = cursor.getString(modifiedDateColumn)

                imageList.add(
                    MTImage(
                        id = id,
                        uri = contentUri,
                        modifiedDate = modifiedDate,
                    )
                )
            }
        }

        return imageList
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
    val modifiedDate: String,
) : Parcelable {
    companion object {
        const val IMAGE_COLUMNS_ID = MediaStore.Images.ImageColumns._ID
        const val IMAGE_COLUMNS_DATE_ADDED = MediaStore.Images.ImageColumns.DATE_ADDED
        const val IMAGE_COLUMNS_MODIFIED_DATE = MediaStore.Images.ImageColumns.DATE_MODIFIED

        fun getInitValues() = MTImage(
            id = -1L,
            uri = "",
            modifiedDate = "",
        )
    }
}