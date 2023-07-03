package com.jinproject.twomillustratedbook.ui.screen.gear

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.billingclient.api.ProductDetails
import com.chargemap.compose.numberpicker.NumberPicker
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.base.item.SnackBarMessage
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultAppBar
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultButton
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultLayout
import com.jinproject.twomillustratedbook.ui.screen.compose.component.HorizontalDivider
import com.jinproject.twomillustratedbook.ui.screen.compose.component.HorizontalSpacer
import com.jinproject.twomillustratedbook.ui.screen.compose.component.VerticalSpacer
import com.jinproject.twomillustratedbook.ui.screen.compose.navigation.BillingModule
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.Typography
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.red
import com.jinproject.twomillustratedbook.utils.TwomIllustratedBookPreview
import com.jinproject.twomillustratedbook.utils.appendBoldText

@Composable
fun GearScreen(
    billingModule: BillingModule,
    gearViewModel: GearViewModel = hiltViewModel(),
    changeVisibilityBottomNavigationBar: (Boolean) -> Unit,
    onNavigatePopBackStack: () -> Unit
) {
    changeVisibilityBottomNavigationBar(false)

    val gearUiState by gearViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarMessage by gearViewModel.snackBarMessage.collectAsStateWithLifecycle(
        initialValue = SnackBarMessage.getInitValues(),
        lifecycleOwner = LocalLifecycleOwner.current
    )

    val availableProducts = remember {
        billingModule.purchasableProducts
    }

    GearScreen(
        gearUiState = gearUiState,
        snackBarMessage = snackBarMessage,
        availableProducts = availableProducts,
        purchaseInApp = billingModule::purchase,
        setIntervalFirstTimerSetting = gearViewModel::setIntervalFirstTimerSetting,
        setIntervalSecondTimerSetting = gearViewModel::setIntervalSecondTimerSetting,
        setIntervalTimerSetting = gearViewModel::setIntervalTimerSetting,
        onNavigatePopBackStack = onNavigatePopBackStack,
        emitSnackBar = gearViewModel::emitSnackBar
    )
}

@Composable
private fun GearScreen(
    gearUiState: GearUiState,
    snackBarMessage: SnackBarMessage,
    availableProducts: List<ProductDetails>,
    context: Context = LocalContext.current,
    purchaseInApp: (ProductDetails) -> Unit,
    setIntervalFirstTimerSetting: (Int) -> Unit,
    setIntervalSecondTimerSetting: (Int) -> Unit,
    setIntervalTimerSetting: () -> Unit,
    onNavigatePopBackStack: () -> Unit,
    emitSnackBar: (SnackBarMessage) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    if (snackBarMessage.headerMessage.isNotBlank())
        LaunchedEffect(key1 = snackBarMessage.headerMessage) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = snackBarMessage.headerMessage,
                actionLabel = snackBarMessage.contentMessage,
                duration = SnackbarDuration.Indefinite
            )
        }

    DefaultLayout(
        topBar = {
            DefaultAppBar(
                title = stringResource(id = R.string.alarm_setting),
                onBackClick = onNavigatePopBackStack
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SettingIntervalItem(
                headerText = stringResource(id = R.string.first),
                pickerValue = gearUiState.intervalSecondTimer,
                onPickerValueChange = { minutes -> setIntervalSecondTimerSetting(minutes) }
            )
            SettingIntervalItem(
                headerText = stringResource(id = R.string.last),
                pickerValue = gearUiState.intervalFirstTimer,
                onPickerValueChange = { minutes -> setIntervalFirstTimerSetting(minutes) }

            )
            DefaultButton(
                content = stringResource(id = R.string.apply_do),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (gearUiState.intervalFirstTimer < gearUiState.intervalSecondTimer) {
                            emitSnackBar(
                                SnackBarMessage(
                                    headerMessage = context.getString(R.string.alarm_setting_interval_failure),
                                    contentMessage = context.getString(R.string.alarm_setting_interval_failure_reason)
                                )
                            )
                        } else {
                            setIntervalTimerSetting()
                            emitSnackBar(
                                SnackBarMessage(
                                    headerMessage = context.getString(R.string.alarm_setting_interval_success)
                                )
                            )
                        }
                    }
            )

            VerticalSpacer(height = 30.dp)
            LazyColumn {
                item {
                    HorizontalDivider()
                    VerticalSpacer(height = 8.dp)
                    Text(
                        text = stringResource(id = R.string.billing_purchase),
                        style = Typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                    )
                    VerticalSpacer(height = 30.dp)
                }
                itemsIndexed(availableProducts) { index, product ->
                    DefaultButton(
                        content = "${product.name} ${stringResource(id = R.string.somethingdo)}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                purchaseInApp(product)
                            }
                    )
                    if (index != availableProducts.lastIndex)
                        VerticalSpacer(height = 8.dp)
                }
            }
        }
    }
}

@Composable
private fun SettingInAppPay(title: String) {
    DefaultButton(content = title)
}

@Composable
private fun SettingIntervalItem(
    headerText: String,
    pickerValue: Int,
    onPickerValueChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = buildAnnotatedString {
                appendBoldText(text = headerText, color = red)
                append(" ${stringResource(id = R.string.alarm_setting_interval)}")
            },
            style = Typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        HorizontalSpacer(width = 16.dp)
        NumberPicker(
            value = pickerValue,
            onValueChange = onPickerValueChange,
            range = 0..59,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.outline
            ),
            dividersColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGearScreen() {
    TwomIllustratedBookPreview {
        GearScreen(
            gearUiState = GearUiState.getInitValue(),
            snackBarMessage = SnackBarMessage.getInitValues(),
            availableProducts = emptyList(),
            purchaseInApp = {},
            setIntervalFirstTimerSetting = {},
            setIntervalSecondTimerSetting = {},
            setIntervalTimerSetting = {},
            onNavigatePopBackStack = {},
            emitSnackBar = {}
        )
    }
}