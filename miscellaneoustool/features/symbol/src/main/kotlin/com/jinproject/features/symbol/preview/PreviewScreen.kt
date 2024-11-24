package com.jinproject.features.symbol.preview

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.design_compose.component.DialogCustom
import com.jinproject.design_compose.component.DialogState
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.VerticalWeightSpacer
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.button.TextButton
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating
import com.jinproject.design_compose.component.text.FooterText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.symbol.getBitmapFromContentUri
import com.jinproject.features.symbol.guildmark.component.ImagePixels
import com.jinproject.features.symbol.guildmark.rememberGuildMarkManager

@Composable
internal fun PreviewScreen(
    detailViewModel: PreviewViewModel = hiltViewModel(),
    billingModule: BillingModule,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
    navigateToGuildMark: (String) -> Unit,
) {
    val imageUri by detailViewModel.imageDetailState.collectAsStateWithLifecycle()
    val purchasableProducts = remember {
        mutableStateListOf<ProductDetails>()
    }

    LaunchedEffect(key1 = Unit) {
        billingModule.getPurchasableProducts(
            listOf(
                BillingModule.Product.SYMBOL_GM
            )
        )?.let { productDetails ->
            purchasableProducts.addAll(productDetails.filterNotNull())
        }
    }
    
    val isSymbolPaid by billingModule.consumeProduct.collectAsStateWithLifecycle()

    LaunchedEffect(isSymbolPaid) {
        if (isSymbolPaid) {
            detailViewModel.setPaidImageUri()
            navigateToGuildMark(imageUri.toString())
            billingModule.updateIsConsumeProduct(false)
        }
    }

    PreviewScreen(
        imageUri = imageUri,
        purchaseInApp = billingModule::purchase,
        purchasableProducts = purchasableProducts,
        popBackStack = popBackStack,
        showSnackBar = showSnackBar,
    )
}

@Composable
private fun PreviewScreen(
    imageUri: Uri,
    context: Context = LocalContext.current,
    configuration: Configuration = LocalConfiguration.current,
    purchasableProducts: List<ProductDetails>,
    purchaseInApp: (ProductDetails) -> Unit,
    popBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val guildMarkManager = rememberGuildMarkManager(
        bitMap = getBitmapFromContentUri(imageUri = imageUri.toString(), context = context),
        slider = 0f
    )

    var showDialogState by remember {
        mutableStateOf(false)
    }
    var dialogState by remember {
        mutableStateOf(DialogState.getInitValue())
    }

    if (showDialogState)
        DialogCustom(
            dialogState = dialogState,
            onDismissRequest = { showDialogState = false }
        )

    DefaultLayout(topBar = {
        BackButtonTitleAppBar(
            title = stringResource(id = R.string.symbol_preview_topbar),
            onBackClick = popBackStack
        )
    }) {
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

        VerticalWeightSpacer(float = 1f)

        TextButton(
            text = stringResource(id = R.string.convert),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            onClick = {
                dialogState = DialogState(
                    header = context.getString(R.string.symbol_preview_dialog_headline),
                    content = context.getString(R.string.symbol_preview_dialog_content),
                    positiveMessage = context.getString(R.string.yes),
                    negativeMessage = context.getString(R.string.no),
                    onPositiveCallback = {
                        purchaseInApp(purchasableProducts.first())
                        showDialogState = false
                    },
                    onNegativeCallback = { showDialogState = false }
                )
                showDialogState = true
            }
        )

        VerticalWeightSpacer(float = 1f)
    }
}

@Preview
@Composable
private fun PreviewPreviewScreen() = MiscellaneousToolTheme {
    PreviewScreen(
        imageUri = Uri.EMPTY,
        purchaseInApp = {},
        purchasableProducts = emptyList(),
        popBackStack = {},
        showSnackBar = {},
    )
}