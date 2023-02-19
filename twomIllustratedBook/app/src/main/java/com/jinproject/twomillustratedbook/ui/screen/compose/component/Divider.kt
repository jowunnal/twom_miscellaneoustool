package com.jinproject.twomillustratedbook.ui.screen.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.lightGray

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = lightGray,
) {
    Spacer(
        modifier = modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color)
    )
}

@Suppress("FunctionName")
fun LazyListScope.VerticalDividerItem(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = lightGray,
) {
    item {
        VerticalDivider(
            modifier = modifier,
            thickness = thickness,
            color = color
        )
    }
}

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = lightGray,
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color)
    )
}

@Suppress("FunctionName")
fun LazyListScope.HorizontalDividerItem(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = lightGray,
) {
    item {
        HorizontalDivider(
            modifier = modifier,
            thickness = thickness,
            color = color
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