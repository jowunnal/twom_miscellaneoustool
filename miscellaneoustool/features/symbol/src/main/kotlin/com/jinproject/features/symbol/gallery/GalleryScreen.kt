package com.jinproject.features.symbol.gallery

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.symbol.gallery.component.GalleryAppBar
import com.jinproject.features.symbol.gallery.component.ImageList
import com.jinproject.features.symbol.guildmark.SymbolOverlayService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
internal fun GalleryScreen(
    galleryViewModel: GalleryViewModel = hiltViewModel(),
    navigateToImageDetail: (String) -> Unit,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    navigateToGuildMarkPreview: (String) -> Unit,
    navigateToGuildMark: (String) -> Unit,
) {
    val images by galleryViewModel.images.collectAsStateWithLifecycle()
    val isPaidImage by galleryViewModel.isPaidImage.collectAsStateWithLifecycle()


    GalleryScreen(
        images = images,
        setClickedImage = galleryViewModel::setClickedImage,
        getMoreImages = galleryViewModel::getMoreImages,
        isPaidImage = isPaidImage,
        navigateToImageDetail = navigateToImageDetail,
        popBackStack = popBackStack,
        showSnackBar = showSnackBar,
        navigateToGuildMarkPreview = navigateToGuildMarkPreview,
        navigateToGuildMark = navigateToGuildMark
    )
}

@Composable
private fun GalleryScreen(
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    images: MTImageList,
    setClickedImage: (Long) -> Unit,
    getMoreImages: (List<MTImage>) -> Unit,
    isPaidImage: Boolean,
    navigateToImageDetail: (String) -> Unit,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    navigateToGuildMarkPreview: (String) -> Unit,
    navigateToGuildMark: (String) -> Unit,
) {
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    val pageCount = 100

    DefaultLayout(
        modifier = Modifier,
        topBar = {
            GalleryAppBar(
                title = stringResource(id = com.jinproject.design_ui.R.string.symbol_gallery_topbar),
                onBackClick = popBackStack,
                enabledTailText = images.clickedId != -1L,
                navigateToGuildMarkPreview = {
                    images.images.find { it.id == images.clickedId }?.let { clickedImage ->
                        context.stopService(
                            Intent(
                                context,
                                SymbolOverlayService::class.java
                            )
                        )
                        if (isPaidImage)
                            navigateToGuildMark(clickedImage.uri)
                        else
                            navigateToGuildMarkPreview(clickedImage.uri)
                    }
                }
            )
        }
    ) {
        if (images.images.isEmpty()) {
            LaunchedEffect(key1 = Unit) {
                getMoreImages(
                    getImageFromContentProvider(
                        resolver = context.contentResolver,
                        selection = "${MTImage.IMAGE_COLUMNS_ID} < ?",
                        selectionArgs = arrayOf(
                            (images.images.lastOrNull()?.id ?: Int.MAX_VALUE).toString(),
                        ),
                        limit = pageCount
                    )
                )
            }
        } else
            ImageList(
                list = images,
                isRefreshing = isRefreshing,
                setClickedImage = { id -> setClickedImage(id) },
                getMoreImages = {
                    coroutineScope.launch {
                        isRefreshing = true
                        delay(500)
                        getMoreImages(
                            getImageFromContentProvider(
                                resolver = context.contentResolver,
                                selection = "${MTImage.IMAGE_COLUMNS_ID} < ?",
                                selectionArgs = arrayOf(
                                    (images.images.lastOrNull()?.id ?: Int.MAX_VALUE).toString(),
                                ),
                                limit = pageCount
                            )
                        )
                        isRefreshing = false
                    }
                },
                navigateToImageDetail = navigateToImageDetail,
            )
    }
}

private suspend fun getImageFromContentProvider(
    resolver: ContentResolver,
    imageUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    limit: Int,
): MutableList<MTImage> {
    val imageList = mutableListOf<MTImage>()

    val projection = arrayOf(
        MTImage.IMAGE_COLUMNS_ID,
    )

    val bundle = Bundle().apply {
        putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
        putInt(ContentResolver.QUERY_ARG_OFFSET, 1)
        putStringArray(
            ContentResolver.QUERY_ARG_SORT_COLUMNS,
            arrayOf(MediaStore.Files.FileColumns.DATE_MODIFIED)
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
            val idColumn = cursor.getColumnIndexOrThrow(MTImage.IMAGE_COLUMNS_ID)

            val id = cursor.getLong(idColumn)
            val contentUri = ContentUris.withAppendedId(imageUri, id).toString()

            imageList.add(
                MTImage(
                    id = id,
                    uri = contentUri,
                )
            )
        }
    }
    return imageList
}

@Preview
@Composable
private fun PreviewGalleryScreen(
    @PreviewParameter(GalleryPreviewParameters::class)
    imageList: MTImageList
) = MiscellaneousToolTheme {
    GalleryScreen(
        images = imageList,
        setClickedImage = {},
        getMoreImages = {},
        isPaidImage = false,
        navigateToImageDetail = {},
        popBackStack = { },
        showSnackBar = {},
        navigateToGuildMarkPreview = {},
        navigateToGuildMark = {}
    )
}