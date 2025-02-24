package com.jinproject.features.info

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R

@Composable
internal fun TermScreen(
    navigatePopBackStack: () -> Unit,
) {
    DefaultLayout(
        topBar = {
            BackButtonTitleAppBar(
                onBackClick = navigatePopBackStack,
                title = stringResource(id = R.string.term)
            )
        }
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context)
            },
            update = { webView ->
                webView.loadUrl("https://sites.google.com/view/miscellanoustool-terms-service/%ED%99%88?authuser=2")
            }
        )
    }
}

@Composable
@Preview
private fun PreviewTermScreen() = MiscellaneousToolTheme {
    TermScreen {}
}