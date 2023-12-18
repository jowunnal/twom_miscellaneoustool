package com.jinproject.features.symbol.gallery

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.symbol.gallery.component.GalleryAppBar
import com.jinproject.features.symbol.gallery.component.ImageList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun GalleryScreen(
    galleryViewModel: GalleryViewModel = hiltViewModel(),
    navigateToImageDetail: (String) -> Unit,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    navigateToGuildMark: (String) -> Unit,
) {
    val images by galleryViewModel.images.collectAsStateWithLifecycle()

    GalleryScreen(
        images = images,
        getMoreImages = galleryViewModel::getMoreImages,
        navigateToImageDetail = navigateToImageDetail,
        popBackStack = popBackStack,
        showSnackBar = showSnackBar,
        navigateToGuildMark = navigateToGuildMark,
    )
}

@Composable
private fun GalleryScreen(
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    images: MTImageList,
    getMoreImages: (List<MTImage>) -> Unit,
    navigateToImageDetail: (String) -> Unit,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    navigateToGuildMark: (String) -> Unit,
) {
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    val pageCount = 50
    var clickedImage by rememberSaveable {
        mutableStateOf(MTImage(-1L, ""))
    }

    DefaultLayout(
        modifier = Modifier,
        topBar = {
            GalleryAppBar(
                title = "갤러리",
                onBackClick = popBackStack,
                enabledTailText = clickedImage.id != -1L,
                navigateToGuildMark = {
                    navigateToGuildMark(clickedImage.uri)
                }
            )
        }
    ) {
        if (images.images.isEmpty()) {
            LaunchedEffect(key1 = Unit) {
                getMoreImages(
                    getImageFromContentProvider(
                        resolver = context.contentResolver,
                        /*selection = "${MTImage.IMAGE_COLUMNS_ID} < ?",
                        selectionArgs = arrayOf(
                            (images.images.lastOrNull()?.id ?: Int.MAX_VALUE).toString(),
                        )*/
                    )
                )
            }
        } else
            ImageList(
                list = images,
                isRefreshing = isRefreshing,
                clickedImage = clickedImage,
                setClickedImage = { id -> clickedImage = id },
                getMoreImages = {
                    coroutineScope.launch {
                        isRefreshing = true
                        getMoreImages(
                            getImageFromContentProvider(
                                resolver = context.contentResolver,
                                /*selection = "${MTImage.IMAGE_COLUMNS_ID} < ?",
                                selectionArgs = arrayOf(
                                    (images.images.lastOrNull()?.id ?: Int.MAX_VALUE).toString(),
                                )*/
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
): MutableList<MTImage> {
    val imageList = mutableListOf<MTImage>()

    val projection = arrayOf(
        MTImage.IMAGE_COLUMNS_ID,
    )

    val query: Cursor? = withContext(Dispatchers.IO) {
        resolver.query(
            imageUri,
            projection,
            selection,
            selectionArgs,
            "${MTImage.IMAGE_COLUMNS_DATE_ADDED} DESC"
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