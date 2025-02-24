package com.jinproject.features.symbol.preview

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.request.ImageRequest
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.jinproject.design_compose.component.DefaultDialog
import com.jinproject.design_compose.component.DialogState
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.VerticalWeightSpacer
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.button.TextButton
import com.jinproject.design_compose.component.getShownDialogState
import com.jinproject.design_compose.component.layout.DefaultDownloadedUiState
import com.jinproject.design_compose.component.layout.DownLoadedUiState
import com.jinproject.design_compose.component.layout.DownloadableLayout
import com.jinproject.design_compose.component.layout.DownloadableUiState
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating
import com.jinproject.design_compose.component.rememberDialogState
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.component.text.FooterText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.BillingModule.Product
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.toProduct
import com.jinproject.features.symbol.getBitmapFromContentUri
import com.jinproject.features.symbol.guildmark.component.ImagePixels
import com.jinproject.features.symbol.guildmark.rememberGuildMarkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun PreviewScreen(
    previewViewModel: PreviewViewModel = hiltViewModel(),
    billingModule: BillingModule,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    navigateToGuildMark: (String) -> Unit,
) {
    val imageUri by previewViewModel.imageDetailState.collectAsStateWithLifecycle()
    var downloadableState: DownloadableUiState by remember {
        mutableStateOf(DefaultDownloadedUiState)
    }

    DisposableEffect(key1 = Unit) {
        val listener = object : BillingModule.OnSuccessListener {
            override fun onSuccess(purchase: Purchase) {
                if (purchase.toProduct() == Product.SYMBOL_GM) {
                    downloadableState = DownloadableUiState.Loading
                    previewViewModel.addPaidSymbol(
                        navigateToGuildMark = {
                            navigateToGuildMark(imageUri)
                        },
                        showSnackBar = showSnackBar,
                    )
                }
            }
        }

        billingModule.addBillingListener(onSuccess = listener)

        onDispose {
            billingModule.removeBillingListener(onSuccess = listener)
        }
    }

    PreviewScreen(
        imageUri = imageUri,
        downloadableState = downloadableState,
        purchaseInApp = billingModule::purchase,
        getPurchasableProducts = billingModule::getPurchasableProducts,
        popBackStack = popBackStack,
        showSnackBar = showSnackBar,
    )
}

@Composable
private fun PreviewScreen(
    imageUri: String,
    downloadableState: DownloadableUiState,
    context: Context = LocalContext.current,
    configuration: Configuration = LocalConfiguration.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    getPurchasableProducts: suspend (List<Product>) -> List<ProductDetails?>?,
    purchaseInApp: (ProductDetails) -> Unit,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val guildMarkManager = rememberGuildMarkManager(
        bitMap = getBitmapFromContentUri(imageUri = imageUri, context = context),
        slider = 0f
    )

    var dialogState by rememberDialogState(
        dialogState = DialogState(
            header = stringResource(R.string.symbol_preview_dialog_headline),
            content = stringResource(R.string.symbol_preview_dialog_content),
            positiveMessage = stringResource(R.string.yes),
            negativeMessage = stringResource(R.string.no),
            onPositiveCallback = {},
            onNegativeCallback = {},
        )
    )

    DefaultDialog(
        dialogState = dialogState,
        onDismissRequest = { dialogState.changeVisibility(false) },
    ) {
        DescriptionSmallText(text = dialogState.content)
    }

    DownloadableLayout(
        topBar = {
            BackButtonTitleAppBar(
                title = stringResource(id = R.string.symbol_preview_topbar),
                onBackClick = popBackStack
            )
        },
        downloadableUiState = downloadableState,
        verticalScrollable = true,
    ) {
        SubcomposeAsyncImageWithPreview(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .build(),
            contentDescription = "Image",
            loading = {
                MTProgressIndicatorRotating()
            },
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height((configuration.screenHeightDp / 2).dp),
            placeHolderPreview = R.drawable.ic_x,
        )

        VerticalSpacer(height = 30.dp)

        Image(
            painter = painterResource(id = R.drawable.ic_arrow_down_long),
            contentDescription = "Arrow Down",
            alignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterHorizontally)
        )
        VerticalSpacer(height = 50.dp)

        ImagePixels(guildMarkManager = guildMarkManager, modifier = Modifier.size(2.dp))
        VerticalSpacer(height = 4.dp)
        FooterText(
            text = stringResource(id = R.string.symbol_preview_expectation),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        VerticalSpacer(height = 30.dp)

        ImagePixels(guildMarkManager = guildMarkManager, modifier = Modifier.size(4.dp))
        VerticalSpacer(height = 4.dp)
        FooterText(
            text = stringResource(id = R.string.symbol_preview_expectation_double),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        VerticalSpacer(height = 30.dp)

        TextButton(
            text = stringResource(id = R.string.convert),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            onClick = {
                dialogState = dialogState.getShownDialogState(
                    onPositiveCallback = {
                        coroutineScope.launch {
                            getPurchasableProducts(
                                listOf(
                                    Product.SYMBOL_GM
                                )
                            )?.filterNotNull()?.first()?.let {
                                purchaseInApp(it)
                            }
                        }
                    },
                )
            }
        )

        VerticalWeightSpacer(float = 1f)
    }
}

@Preview
@Composable
private fun PreviewPreviewScreen() = MiscellaneousToolTheme {
    PreviewScreen(
        imageUri = "",
        downloadableState = object : DownLoadedUiState<Any>() {
            override val data: Any
                get() = Any()

        },
        purchaseInApp = {},
        getPurchasableProducts = { emptyList() },
        popBackStack = {},
        showSnackBar = {},
    )
}