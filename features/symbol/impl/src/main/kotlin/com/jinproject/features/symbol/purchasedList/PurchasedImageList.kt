package com.jinproject.features.symbol.purchasedList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.symbol.ImageListPreviewData
import com.jinproject.features.symbol.gallery.MTImage
import com.jinproject.features.symbol.gallery.component.ImageList
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.CoroutineScope

@Composable
fun PurchasedImageList(
    imageList: PersistentList<MTImage>,
    getNextPage: () -> Unit,
    navigateToItemDetail: (String) -> Unit,
    navigateToGuildMark: (String) -> Unit,
) {
    ImageList(
        list = imageList,
        onClickImage = { image ->
            navigateToGuildMark(image.uri)
        },
        isRefreshing = false,
        getMoreImages = {
            getNextPage()
        },
        navigateToImageDetail = navigateToItemDetail,
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewPurchasedImageList() = MiscellaneousToolTheme {
    PurchasedImageList(
        imageList = ImageListPreviewData.items,
        getNextPage = {},
        navigateToItemDetail = {},
        navigateToGuildMark = {},
    )
}