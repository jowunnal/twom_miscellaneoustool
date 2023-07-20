package com.jinproject.twomillustratedbook.ui.screen.gear

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.chargemap.compose.numberpicker.NumberPicker
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.component.DefaultAppBar
import com.jinproject.design_compose.component.DefaultButton
import com.jinproject.design_compose.component.DefaultLayout
import com.jinproject.design_compose.component.HorizontalDivider
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.red
import com.jinproject.design_compose.theme.Typography
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.utils.appendBoldText
import com.jinproject.features.core.utils.findActivity
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun GearScreen(
    gearViewModel: GearViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    changeVisibilityBottomNavigationBar: (Boolean) -> Unit,
    onNavigatePopBackStack: () -> Unit,
    showSnackBar: suspend (SnackBarMessage) -> Unit
) {
    changeVisibilityBottomNavigationBar(false)

    val gearUiState by gearViewModel.uiState.collectAsStateWithLifecycle()

    val callback = object: MainActivity.OnBillingCallback {
        override fun onSuccess(purchase: Purchase) {
            gearViewModel::emitSnackBar.invoke(
                SnackBarMessage(
                    headerMessage = "${purchase.products.first()} 상품의 구매가 완료되었어요."
                )
            )
        }

        override fun onFailure(errorCode: Int) {
            Log.e("test","error : $errorCode")
            val snackBar = SnackBarMessage(
                headerMessage = "구매 실패",
                contentMessage = when (errorCode) {
                    1 -> "취소를 하셨어요."
                    2, 3, 4 -> "유효하지 않은 상품 이에요."
                    5, 6 -> "잘못된 상품 이에요."
                    7 -> "이미 보유하고 있는 상품 이에요."
                    else -> "네트워크 에러로 인해 실패했어요."
                }
            )
            gearViewModel::emitSnackBar.invoke(snackBar)
        }

    }

    val activity = (context.findActivity() as MainActivity).apply {
        setBillingCallback(callback)
    }
    val billingModule = activity.billingModule

    val availableProducts = remember {
        billingModule.purchasableProducts
    }

    GearScreen(
        gearUiState = gearUiState,
        availableProducts = availableProducts,
        purchaseInApp = billingModule::purchase,
        setIntervalFirstTimerSetting = gearViewModel::setIntervalFirstTimerSetting,
        setIntervalSecondTimerSetting = gearViewModel::setIntervalSecondTimerSetting,
        setIntervalTimerSetting = gearViewModel::setIntervalTimerSetting,
        onNavigatePopBackStack = onNavigatePopBackStack,
        showSnackBar = showSnackBar
    )
}

@Composable
private fun GearScreen(
    gearUiState: GearUiState,
    availableProducts: List<ProductDetails>,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    context: Context = LocalContext.current,
    purchaseInApp: (ProductDetails) -> Unit,
    setIntervalFirstTimerSetting: (Int) -> Unit,
    setIntervalSecondTimerSetting: (Int) -> Unit,
    setIntervalTimerSetting: () -> Unit,
    onNavigatePopBackStack: () -> Unit,
    showSnackBar: suspend (SnackBarMessage) -> Unit
) {

    DefaultLayout(
        topBar = {
            DefaultAppBar(
                title = stringResource(id = R.string.alarm_setting),
                onBackClick = onNavigatePopBackStack
            )
        },
        content = {
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
                        coroutineScope.launch {
                            if (gearUiState.intervalFirstTimer < gearUiState.intervalSecondTimer) {
                                showSnackBar(
                                    SnackBarMessage(
                                        headerMessage = context.getString(R.string.alarm_setting_interval_failure),
                                        contentMessage = context.getString(R.string.alarm_setting_interval_failure_reason)
                                    )
                                )
                            } else {
                                setIntervalTimerSetting()
                                showSnackBar(
                                    SnackBarMessage(
                                        headerMessage = context.getString(R.string.alarm_setting_interval_success)
                                    )
                                )
                            }
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
    )
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
                appendBoldText(text = headerText, color = red.color)
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
    PreviewMiscellaneousToolTheme {
        GearScreen(
            gearUiState = GearUiState.getInitValue(),
            availableProducts = emptyList(),
            purchaseInApp = {},
            setIntervalFirstTimerSetting = {},
            setIntervalSecondTimerSetting = {},
            setIntervalTimerSetting = {},
            onNavigatePopBackStack = {},
            showSnackBar = {}
        )
    }
}