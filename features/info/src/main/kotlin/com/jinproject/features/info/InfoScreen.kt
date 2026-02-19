package com.jinproject.features.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.billingclient.api.ProductDetails
import com.jinproject.design_compose.component.DefaultDialog
import com.jinproject.design_compose.component.DialogState
import com.jinproject.design_compose.component.HorizontalDivider
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.component.getShownDialogState
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.component.rememberDialogState
import com.jinproject.design_compose.component.text.DescriptionMediumText
import com.jinproject.design_compose.component.text.HeadlineText
import com.jinproject.design_compose.component.text.auth.PasswordField
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.auth.AuthRoute
import com.jinproject.features.core.AuthManager
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.BillingModule.Product
import com.jinproject.features.core.compose.LocalNavigator
import com.jinproject.features.core.compose.LocalShowSnackbar
import com.jinproject.features.info.state.InfoUiState
import com.jinproject.features.info.state.rememberInfoUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun InfoScreen(
    billingModule: BillingModule,
) {
    val navigator = LocalNavigator.current
    val showSnackBar = LocalShowSnackbar.current
    val infoUiState = rememberInfoUiState(
        isUserActive = AuthManager.isActive,
        showSnackBar = showSnackBar,
        pwCheck = null
    )

    InfoScreen(
        infoUiState = infoUiState,
        getPurchasableProducts = billingModule::getPurchasableProducts,
        purchase = billingModule::purchase,
        navigateRoute = { route -> navigator.navigate(route) },
        navigateToAuthGraph = { navigator.navigate(AuthRoute.SignIn) },
    )

}

@Composable
private fun InfoScreen(
    infoUiState: InfoUiState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    getPurchasableProducts: suspend (List<Product>) -> List<ProductDetails?>?,
    purchase: (ProductDetails) -> Unit,
    navigateRoute: (InfoRoute) -> Unit,
    navigateToAuthGraph: () -> Unit,
) {
    var dialogState by rememberDialogState(
        DialogState(
            header = stringResource(id = R.string.auth_sign_password),
            positiveMessage = stringResource(id = R.string.confirm),
            negativeMessage = stringResource(id = R.string.exit),
            onNegativeCallback = {},
            onPositiveCallback = {},
        )
    )

    val purchasableProducts: List<ProductDetails> by produceState(initialValue = listOf()) {
        value = getPurchasableProducts(
            listOf(
                BillingModule.Product.AD_REMOVE,
                BillingModule.Product.SUPPORT,
            )
        )?.filterNotNull() ?: emptyList()
    }

    DefaultDialog(
        dialogState = dialogState,
        onDismissRequest = {
            infoUiState.pw.textFieldState.clearText()
            dialogState.changeVisibility(false)
        },
    ) {
        PasswordField(authTextFieldState = infoUiState.pw)
    }

    DefaultLayout(
        topBar = {
            HeadlineText(
                text = stringResource(id = R.string.settings),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
                    .padding(vertical = 30.dp),
            )
        }
    ) {
        if (LocalInspectionMode.current || infoUiState.isActive) {
            InfoItem(
                title = stringResource(id = R.string.change_info),
                imageVector = ImageVector.vectorResource(R.drawable.ic_account_circle),
                onClicked = {
                    dialogState = dialogState.getShownDialogState(
                        onPositiveCallback = {
                            coroutineScope.launch {
                                infoUiState.checkUserPassword {
                                    navigateRoute(InfoRoute.InfoChange)
                                }
                                infoUiState.pw.textFieldState.clearText()
                            }
                        },
                    )
                },
            )

            InfoItem(
                title = stringResource(id = R.string.logout),
                imageVector = ImageVector.vectorResource(R.drawable.ic_exit_to_app),
                onClicked = {
                    coroutineScope.launch {
                        infoUiState.logOut()
                        infoUiState.changeIsActive(false)
                    }
                },
            )

            InfoItem(
                title = stringResource(id = R.string.withdrawal),
                imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                onClicked = {
                    dialogState = dialogState.getShownDialogState(
                        onPositiveCallback = {
                            coroutineScope.launch {
                                infoUiState.withdrawal()
                                infoUiState.changeIsActive(false)
                                infoUiState.pw.textFieldState.clearText()
                            }
                        },
                    )
                },
            )
        } else
            InfoItem(
                title = stringResource(id = R.string.login),
                imageVector = ImageVector.vectorResource(R.drawable.ic_account_circle),
                onClicked = navigateToAuthGraph,
            )

        InfoItem(
            title = stringResource(id = R.string.term),
            imageVector = ImageVector.vectorResource(R.drawable.ic_info),
            onClicked = {
                navigateRoute(InfoRoute.Term)
            },
        )

        purchasableProducts.forEach { product ->
            InfoItem(
                title = "${product.name} ${stringResource(id = R.string.somethingdo)}",
                imageVector = ImageVector.vectorResource(R.drawable.ic_shopping),
                onClicked = {
                    purchase(product)
                },
            )
        }
    }
}

@Composable
internal fun InfoItem(
    modifier: Modifier = Modifier,
    title: String,
    imageVector: ImageVector,
    onClicked: () -> Unit,
) {
    Column(
        modifier = modifier.clickableAvoidingDuplication(onClick = onClicked),
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                imageVector = imageVector,
                contentDescription = "$title Icon",
            )

            DescriptionMediumText(
                text = title,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        HorizontalDivider()
    }
}

@Composable
@Preview
private fun PreviewInfoScreen() = MiscellaneousToolTheme {
    InfoScreen(
        infoUiState = rememberInfoUiState(isUserActive = true, showSnackBar = {}),
        getPurchasableProducts = { emptyList() },
        purchase = {},
        navigateRoute = {},
        navigateToAuthGraph = {},
    )
}
