package com.jinproject.features.droplist.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.core.utils.toAssetImageUri
import com.jinproject.features.droplist.DropListUiState
import com.jinproject.features.droplist.DropListUiStatePreviewParameter

@Composable
internal fun ColumnScope.Monster(
    modifier: Modifier,
    imgName: String,
    header: String,
    tail: String? = null,
) {
    SubcomposeAsyncImageWithPreview(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(toAssetImageUri(prefix = "monster", imgName = imgName))
            .build(),
        contentDescription = "Image",
        loading = {
            MTProgressIndicatorRotating()
        },
        contentScale = ContentScale.Fit,
        modifier = modifier,
        placeHolderPreview = com.jinproject.design_ui.R.drawable.test,
    )
    VerticalSpacer(height = 4.dp)
    DescriptionSmallText(text = header)
    tail?.let {
        VerticalSpacer(height = 4.dp)
        DescriptionSmallText(text = tail)
    }
    VerticalSpacer(height = 4.dp)
}

@Composable
@Preview
private fun MonsterPreview(
    @PreviewParameter(DropListUiStatePreviewParameter::class)
    dropListUiState: DropListUiState,
) = MiscellaneousToolTheme {
   Column {
       Monster(
           modifier = Modifier,
           imgName = dropListUiState.selectedMap.imgName,
           header = dropListUiState.selectedMap.name,
       )
   }
}