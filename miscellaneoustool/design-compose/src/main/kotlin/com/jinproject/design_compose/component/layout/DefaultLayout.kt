package com.jinproject.design_compose.component.layout

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.paddingvalues.MiscellanousToolPaddingValues
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating

@Composable
inline fun DefaultLayout(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    contentPaddingValues: MiscellanousToolPaddingValues = MiscellanousToolPaddingValues(),
    isLoading: Boolean = false,
    verticalScrollable: Boolean = false,
    crossinline content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        topBar()
        VerticalSpacer(height = 8.dp)

        Crossfade(
            targetState = isLoading,
            label = "Crossfade Default Layout",
            modifier = Modifier
                .padding(contentPaddingValues)
                .weight(1f)
                .imePadding()
        ) { loading ->
            if (loading)
                MTProgressIndicatorRotating(modifier = Modifier)
            else
                Column(
                    modifier = Modifier.fillMaxSize().then(
                        if (verticalScrollable)
                            Modifier.verticalScroll(rememberScrollState())
                        else
                            Modifier
                    )
                ) {
                    content()
                }
        }
    }
}