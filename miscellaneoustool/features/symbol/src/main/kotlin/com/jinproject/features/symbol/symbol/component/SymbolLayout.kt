package com.jinproject.features.symbol.symbol.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.DescriptionSmallText
import com.jinproject.design_compose.component.HeadlineText
import com.jinproject.design_compose.component.VerticalSpacer

@Composable
internal fun SymbolLayout(
    modifier: Modifier = Modifier,
    headline: String,
    headLineContent: @Composable ColumnScope.() -> Unit = {},
    desc: String,
    descriptionContent: @Composable ColumnScope.() -> Unit = {},
    footer: String,
    footerContent: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 32.dp, horizontal = 20.dp),
    ) {
        HeadlineText(
            text = headline,
            maxLines = 2
        )
        VerticalSpacer(height = 50.dp)
        headLineContent()
        VerticalSpacer(height = 50.dp)
        DescriptionSmallText(text = desc)
        VerticalSpacer(height = 50.dp)
        descriptionContent()
        VerticalSpacer(height = 50.dp)
        DescriptionSmallText(text = footer)
        VerticalSpacer(height = 50.dp)
        footerContent()
    }
}