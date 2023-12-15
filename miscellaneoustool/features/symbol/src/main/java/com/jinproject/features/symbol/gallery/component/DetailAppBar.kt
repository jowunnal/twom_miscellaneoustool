package com.jinproject.features.symbol.gallery.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.R
import com.jinproject.design_compose.component.AppBarText
import com.jinproject.design_compose.component.DescriptionText
import com.jinproject.design_compose.component.OneButtonAppBar

@Composable
fun GalleryAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    enabledTailText: Boolean,
    navigateToGuildMark: () -> Unit,
) {
    val visibility by animateFloatAsState(targetValue = if (enabledTailText) 1f else 0f, label = "")

    OneButtonAppBar(
        modifierAppBar = modifier,
        modifierButton = Modifier,
        buttonAlignment = Alignment.CenterStart,
        icon = R.drawable.ic_arrow_left,
        onClick = onBackClick
    ) {
        AppBarText(
            text = title,
            modifier = Modifier
                .align(Alignment.Center),
        )
        DescriptionText(
            text = "전송",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    if(visibility == 1f)
                        navigateToGuildMark()
                }
                .padding(horizontal = 4.dp)
                .graphicsLayer {
                    alpha = visibility
                },
        )
    }
}