package com.jinproject.twomillustratedbook.ui.screen.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.Typography
import com.jinproject.twomillustratedbook.utils.TwomIllustratedBookPreview

@Composable
fun DefaultAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onBackClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(4.dp, RectangleShape, clip = false)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(
                    onClick = onBackClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false, radius = 18.dp)
                ),
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = "back",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = title,
            style = Typography.headlineSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun PreviewDefaultAppBar() = TwomIllustratedBookPreview {
    DefaultAppBar(
        title = "앱바 타이틀",
        onBackClick = {}
    )
}