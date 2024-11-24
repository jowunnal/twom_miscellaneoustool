package com.jinproject.features.symbol.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.design_compose.component.bar.BackButtonRowScopeAppBar
import com.jinproject.design_compose.component.button.DefaultIconButton
import com.jinproject.design_compose.component.lazyList.ScrollableLayout
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorInfiniteRotating
import com.jinproject.design_compose.component.text.ChatTextField
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_compose.tu
import com.jinproject.features.symbol.gallery.component.GalleryImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
internal fun GenerateImageScreen(
    viewModel: GenerateImageViewModel = hiltViewModel(),
    navigateToBack: () -> Unit,
    navigateToImageDetail: (String) -> Unit,
    navigateToGuildMarkPreview: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GenerateImageScreen(
        uiState = uiState,
        generateImage = viewModel::generateImage,
        navigateToBack = navigateToBack,
        navigateToImageDetail = navigateToImageDetail,
        navigateToGuildMarkPreview = navigateToGuildMarkPreview,
        downloadImage = viewModel::downloadImage,
    )
}

@Composable
private fun GenerateImageScreen(
    uiState: GenerateImageUiState,
    generateImage: (String) -> Unit,
    navigateToBack: () -> Unit,
    navigateToImageDetail: (String) -> Unit,
    navigateToGuildMarkPreview: (String) -> Unit,
    downloadImage: (Message) -> Unit,
) {
    val textFieldState = rememberTextFieldState()
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = uiState.messages.size) {
        if (uiState.messages.isNotEmpty())
            lazyListState.animateScrollToItem(uiState.messages.lastIndex)
    }

    val viewPortSize by remember {
        derivedStateOf {
            lazyListState.layoutInfo.viewportSize
        }
    }

    DefaultLayout(
        topBar = {
            BackButtonRowScopeAppBar(onBackClick = navigateToBack)
        },
    ) {
        ScrollableLayout(
            scrollableState = lazyListState,
            viewHeight = with(LocalDensity.current) {
                val perViewPortHeight = viewPortSize.height

                val maxItemHeight =
                    uiState.messages.sumOf {
                        when (it.publisher) {
                            Publisher.User -> (it.data.length * 12.tu.toPx() / 200.dp.toPx() + 1) * 14.tu.toPx()
                                .toDouble() + 18.dp.toPx()

                            Publisher.AI -> 100.dp.toPx().toDouble() + 8.dp.toPx()
                        }
                    }

                maxItemHeight.toFloat() - perViewPortHeight
            },
            startFromTop = false,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                state = lazyListState,
            ) {
                items(
                    uiState.messages,
                    key = { message -> message.timeStamp },
                ) { message ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (message.publisher == Publisher.User) Arrangement.End else Arrangement.Start,
                    ) {
                        if (message.publisher == Publisher.User) {
                            DescriptionSmallText(
                                text = message.data,
                                modifier = Modifier
                                    .widthIn(max = 200.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(vertical = 10.dp, horizontal = 12.dp)
                                    .animateItem(),
                            )
                        } else {
                            GalleryImage(
                                uri = message.data,
                                downloadState = message.downloadState,
                                enlargeImage = { navigateToImageDetail(message.data) },
                                modifier = Modifier
                                    .size(100.dp)
                                    .graphicsLayer {
                                        alpha =
                                            if (message.downloadState is DownloadState.Downloading) 0.3f else 1f
                                    }
                                    .animateItem(),
                                onClickImage = { isError ->
                                    when (message.downloadState) {
                                        is DownloadState.Downloaded -> {
                                            if (!isError) {
                                                navigateToGuildMarkPreview(message.data)
                                            }
                                        }

                                        DownloadState.NotDownloaded -> {
                                            if (!isError)
                                                coroutineScope.launch(Dispatchers.IO) {
                                                    downloadImage(message)
                                                }
                                        }

                                        is DownloadState.Downloading -> {}
                                    }
                                },
                                content = { isError, downloadState ->
                                    if (downloadState is DownloadState.Downloading) {
                                        Box(
                                            modifier = Modifier
                                                .size(100.dp)
                                                .align(Alignment.Center),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            MTProgressIndicatorInfiniteRotating()
                                        }
                                    } else if (downloadState is DownloadState.NotDownloaded && !isError) {
                                        DefaultIconButton(
                                            icon = com.jinproject.design_ui.R.drawable.ic_download_file,
                                            onClick = {
                                                coroutineScope.launch(Dispatchers.IO) {
                                                    downloadImage(message)
                                                }
                                            },
                                            modifier = Modifier
                                                .background(
                                                    MaterialTheme.colorScheme.primary,
                                                    RoundedCornerShape(2.5.dp),
                                                )
                                                .padding(2.dp)
                                                .align(Alignment.TopEnd),
                                            iconSize = 24.dp,
                                            backgroundTint = Color.Unspecified,
                                            iconTint = MaterialTheme.colorScheme.onPrimary,
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        ChatTextField(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding(),
            textFieldState = textFieldState,
            onEntered = {
                val prompt = textFieldState.text.toString()
                generateImage(prompt)
                textFieldState.clearText()
            },
        )
    }
}

@Composable
@Preview
private fun PreviewGenerateImageScreen(
    @PreviewParameter(GenerateImageUiStatePreviewParameter::class)
    uiState: GenerateImageUiState,
) = MiscellaneousToolTheme {
    GenerateImageScreen(
        uiState = uiState,
        generateImage = {},
        navigateToBack = {},
        navigateToImageDetail = {},
        navigateToGuildMarkPreview = {},
        downloadImage = {},
    )
}