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
import com.jinproject.design_compose.component.layout.DownLoadedUiState
import com.jinproject.design_compose.component.layout.DownloadableUiState
import com.jinproject.domain.repository.SymbolRepository
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.utils.RestartableStateFlow
import com.jinproject.features.core.utils.restartableStateIn
import com.jinproject.features.symbol.gallery.MTImage.Companion.IMAGE_COLUMNS_ID
import com.jinproject.features.symbol.gallery.MTImage.Companion.IMAGE_COLUMNS_MODIFIED_DATE
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
internal class GalleryViewModel @Inject constructor(
    @ApplicationContext context: Context,
    repository: SymbolRepository,
) : ViewModel() {

    val snackBarMessageChannel = Channel<SnackBarMessage>(Channel.CONFLATED)

    private var lastModifiedDate: String? = Instant.now().toEpochMilli().toString()
    private var origin = arrayListOf<MTImage>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val galleryUiState: RestartableStateFlow<DownloadableUiState> =
        repository.getPaidSymbolUris().flatMapLatest { paidSymbolUris ->
            flow {
                val images = (lastModifiedDate?.let {
                    getImageFromContentProvider(
                        resolver = context.contentResolver,
                        selection = "$IMAGE_COLUMNS_MODIFIED_DATE < ?",
                        selectionArgs = arrayOf(it),
                    )
                } ?: emptyList()).also { list ->
                    lastModifiedDate = list.lastOrNull()?.modifiedDate
                }

                emit(
                    GalleryUiState(
                        data = origin.apply {
                            if (images.isNotEmpty())
                                addAll(images)
                        }.toPersistentList(),
                        paidImageUris = paidSymbolUris.toPersistentList(),
                    )
                )
            }
        }.restartableStateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DownloadableUiState.Loading,
        )

    fun getMoreImages() {
        galleryUiState.stopAndStart()
    }

    private suspend fun getImageFromContentProvider(
        resolver: ContentResolver,
        imageUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        limit: Int = 100,
    ): PersistentList<MTImage> {
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

        return imageList.toPersistentList()
    }
}

@Stable
internal data class GalleryUiState(
    override val data: ImmutableList<MTImage>,
    val paidImageUris: ImmutableList<String>,
) : DownLoadedUiState<ImmutableList<MTImage>>() {
    fun isPaidImage(image: MTImage): Boolean = image.uri in paidImageUris
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