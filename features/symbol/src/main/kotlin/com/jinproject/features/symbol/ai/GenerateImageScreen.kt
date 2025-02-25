package com.jinproject.features.symbol.ai

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.billingclient.api.Purchase
import com.jinproject.design_compose.clearFocusIfKeyboardActive
import com.jinproject.design_compose.component.DialogState
import com.jinproject.design_compose.component.TextDialog
import com.jinproject.design_compose.component.bar.BackButtonRowScopeAppBar
import com.jinproject.design_compose.component.button.DefaultIconButton
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.component.getShownDialogState
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.component.lazyList.ScrollableLayout
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorInfiniteRotating
import com.jinproject.design_compose.component.rememberDialogState
import com.jinproject.design_compose.component.text.ChatTextField
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_compose.tu
import com.jinproject.features.core.AuthManager
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.RealtimeDatabaseManager
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.toProduct
import com.jinproject.features.symbol.gallery.component.GalleryImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
internal fun GenerateImageScreen(
    viewModel: GenerateImageViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    billingModule: BillingModule,
    navigateToBack: () -> Unit,
    navigateToImageDetail: (String) -> Unit,
    navigateToGuildMarkPreview: (String) -> Unit,
    navigateToPurchasedImage: () -> Unit,
    navigateToAuthGraph: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val aiTokenCounter by viewModel.aiTokenCounter.collectAsStateWithLifecycle()

    DisposableEffect(key1 = Unit) {
        val listener = object : BillingModule.OnSuccessListener {
            override fun onSuccess(purchase: Purchase) {
                coroutineScope.launch(Dispatchers.IO) {
                    if (purchase.toProduct() == BillingModule.Product.AI_TOKEN)
                        RealtimeDatabaseManager.incToken(origin = aiTokenCounter)
                }
            }
        }

        billingModule.addBillingListener(onSuccess = listener)

        onDispose {
            billingModule.removeBillingListener(onSuccess = listener)
        }
    }

    GenerateImageScreen(
        uiState = uiState,
        aiTokenCounter = aiTokenCounter,
        generateImage = viewModel::generateImage,
        navigateToBack = navigateToBack,
        navigateToImageDetail = navigateToImageDetail,
        navigateToGuildMarkPreview = navigateToGuildMarkPreview,
        downloadImage = viewModel::downloadImage,
        getNextPage = viewModel::getNextPage,
        navigateToPurchasedImage = navigateToPurchasedImage,
        navigateToAuthGraph = navigateToAuthGraph,
        showSnackBar = showSnackBar,
        purchaseInApp = {
            coroutineScope.launch {
                billingModule.getPurchasableProducts(listOf(BillingModule.Product.AI_TOKEN))
                    ?.filterNotNull()?.first()?.let {
                        billingModule.purchase(it)
                    }
            }
        }
    )
}

@Composable
private fun GenerateImageScreen(
    uiState: GenerateImageUiState,
    aiTokenCounter: Long,
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    generateImage: (String) -> Unit,
    navigateToBack: () -> Unit,
    navigateToImageDetail: (String) -> Unit,
    navigateToGuildMarkPreview: (String) -> Unit,
    downloadImage: (Message) -> Unit,
    getNextPage: () -> Unit,
    navigateToPurchasedImage: () -> Unit,
    navigateToAuthGraph: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    purchaseInApp: () -> Unit,
) {
    val textFieldState = rememberTextFieldState()
    val lazyListState = rememberLazyListState()

    if (uiState.messages.isNotEmpty())
        LaunchedEffect(key1 = uiState.messages.last().timeStamp) {
            lazyListState.animateScrollToItem(uiState.messages.lastIndex)
        }

    val viewPortEndOffset by remember {
        derivedStateOf {
            lazyListState.layoutInfo.viewportEndOffset
        }
    }

    var dialogState by rememberDialogState(
        DialogState(
            header = stringResource(com.jinproject.design_ui.R.string.symbol_ai_dialog_purchase_token_header),
            content = stringResource(com.jinproject.design_ui.R.string.symbol_ai_dialog_purchase_token_content),
            positiveMessage = stringResource(com.jinproject.design_ui.R.string.symbol_ai_dialog_purchase_token_yes),
            negativeMessage = stringResource(com.jinproject.design_ui.R.string.no),
            onPositiveCallback = {},
            onNegativeCallback = {},
        )
    )

    TextDialog(
        dialogState = dialogState,
        onDismissRequest = { dialogState.changeVisibility(false) }
    )

    DefaultLayout(
        topBar = {
            BackButtonRowScopeAppBar(onBackClick = navigateToBack) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = Modifier
                        .clickableAvoidingDuplication {
                            if (AuthManager.isActive)
                                dialogState = dialogState.getShownDialogState(
                                    onPositiveCallback = purchaseInApp,
                                )
                            else {
                                navigateToAuthGraph()
                                showSnackBar(
                                    SnackBarMessage(
                                        headerMessage = context.getString(com.jinproject.design_ui.R.string.auth_sign_in_required)
                                    )
                                )
                            }
                        }
                        .padding(end = 16.dp),
                    painter = painterResource(id = com.jinproject.design_ui.R.drawable.ic_shopping),
                    contentDescription = "Shopping Icon",
                )
                Image(
                    modifier = Modifier
                        .clickableAvoidingDuplication {
                            if (AuthManager.isActive)
                                navigateToPurchasedImage()
                            else {
                                navigateToAuthGraph()
                                showSnackBar(
                                    SnackBarMessage(
                                        headerMessage = context.getString(com.jinproject.design_ui.R.string.auth_sign_in_required)
                                    )
                                )
                            }
                        }
                        .padding(end = 12.dp),
                    painter = painterResource(id = com.jinproject.design_ui.R.drawable.ic_history),
                    contentDescription = "Purchase History",
                )
            }
        },
        modifier = Modifier
            .clearFocusIfKeyboardActive()
            .nestedScroll(object : NestedScrollConnection {
                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val new = if (available.y > 0f) {
                        getNextPage()

                        0f
                    } else
                        available.y

                    return available.copy(y = new)
                }
            })
    ) {
        ScrollableLayout(
            scrollableState = lazyListState,
            viewHeight = with(LocalDensity.current) {
                val perViewPortHeight = viewPortEndOffset

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
            isUpperScrollActive = true,
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
                        horizontalArrangement = if (message.publisher == Publisher.User)
                            Arrangement.End
                        else
                            Arrangement.Start,
                    ) {
                        when (message.publisher) {
                            Publisher.User ->
                                DescriptionSmallText(
                                    text = message.data,
                                    modifier = Modifier
                                        .widthIn(max = 200.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                        .padding(vertical = 10.dp, horizontal = 12.dp)
                                        .animateItem(),
                                )

                            Publisher.AI -> {
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
                                                    downloadImage(message)
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
                                                    downloadImage(message)
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

                item {
                    DescriptionSmallText(
                        text = "${stringResource(id = com.jinproject.design_ui.R.string.symbol_ai_remain_token)}: $aiTokenCounter",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth()
                            .padding(vertical = 12.dp),
                    )
                }
            }
        }

        ChatTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .imePadding(),
            textFieldState = textFieldState,
            onEntered = {
                if (aiTokenCounter > 0) {
                    coroutineScope.launch {
                        RealtimeDatabaseManager.decToken(aiTokenCounter)
                    }
                    val prompt = textFieldState.text.toString()
                    generateImage(prompt)
                    textFieldState.clearText()
                } else
                    dialogState = dialogState.getShownDialogState(
                        onPositiveCallback = purchaseInApp,
                    )
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
        aiTokenCounter = 0L,
        generateImage = {},
        navigateToBack = {},
        navigateToImageDetail = {},
        navigateToGuildMarkPreview = {},
        downloadImage = {},
        getNextPage = {},
        navigateToPurchasedImage = {},
        navigateToAuthGraph = {},
        showSnackBar = {},
        purchaseInApp = {},
    )
}