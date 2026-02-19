package com.jinproject.design_compose.component.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_ui.R

@Composable
fun DefaultPainterImage(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    @DrawableRes resId: Int,
    contentDescription: String,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
) {
    Image(
        painter = painterResource(id = resId),
        contentDescription = contentDescription,
        modifier = modifier
            .size(size),
        alpha = alpha,
        colorFilter = colorFilter,
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewDefaultImage() = PreviewMiscellaneousToolTheme {
    DefaultPainterImage(
        resId = R.drawable.icon_simulator,
        contentDescription = "",
    )
}