package com.jinproject.design_compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.scrim,
) {
    Spacer(
        modifier = modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color)
    )
}

@Suppress("FunctionName")
@Composable
fun LazyItemScope.VerticalDividerItem(
    idx: Int,
    lastIdx: Int,
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.scrim,
) {
    if(idx != lastIdx) {
        androidx.compose.material3.VerticalDivider(
            modifier = modifier,
            thickness = thickness,
            color = color,
        )
    }
}

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.scrim,
) {
    androidx.compose.material3.HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}

@Suppress("FunctionName")
@Composable
fun LazyItemScope.HorizontalDividerItem(
    idx: Int,
    lastIdx: Int,
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.scrim,
) {
    if(idx != lastIdx) {
        androidx.compose.material3.HorizontalDivider(
            modifier = modifier,
            thickness = thickness,
            color = color,
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
private fun VerticalDividerPreview() {
    Box(contentAlignment = Alignment.Center) {
        VerticalDivider()
    }

}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
private fun HorizontalDividerPreview() {
    Box(contentAlignment = Alignment.Center) {
        HorizontalDivider()
    }
}