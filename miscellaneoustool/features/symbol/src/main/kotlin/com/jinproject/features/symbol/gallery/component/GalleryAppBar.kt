package com.jinproject.features.symbol.gallery.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.AppBarText
import com.jinproject.design_compose.component.DescriptionSmallText
import com.jinproject.design_compose.component.OneButtonAppBar

@Composable
fun GalleryAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    enabledTailText: Boolean,
    navigateToGuildMarkPreview: () -> Unit,
) {
    val visibility by animateFloatAsState(targetValue = if (enabledTailText) 1f else 0f, label = "")

    OneButtonAppBar(
        buttonAlignment = Alignment.CenterStart,
        icon = com.jinproject.design_ui.R.drawable.ic_arrow_left,
        onClick = onBackClick
    ) {
        AppBarText(
            text = title,
            modifier = Modifier
                .align(Alignment.Center),
        )
        DescriptionSmallText(
            text = stringResource(id = com.jinproject.design_ui.R.string.convert),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    if (visibility == 1f)
                        navigateToGuildMarkPreview()
                }
                .padding(horizontal = 4.dp)
                .graphicsLayer {
                    alpha = visibility
                },
            color = MaterialTheme.colorScheme.primary
        )
    }
}