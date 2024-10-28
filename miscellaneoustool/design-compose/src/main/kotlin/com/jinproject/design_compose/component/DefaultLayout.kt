package com.jinproject.design_compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.paddingvalues.MiscellanousToolPaddingValues

@Composable
fun DefaultLayout(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    contentPaddingValues: MiscellanousToolPaddingValues = MiscellanousToolPaddingValues(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        topBar()
        VerticalSpacer(height = 8.dp)
        Column(modifier = Modifier.padding(contentPaddingValues)) {
            content()
        }
    }
}