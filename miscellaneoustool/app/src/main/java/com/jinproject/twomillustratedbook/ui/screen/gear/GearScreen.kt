package com.jinproject.twomillustratedbook.ui.screen.gear

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.billingclient.api.ProductDetails
import com.chargemap.compose.numberpicker.NumberPicker
import com.jinproject.twomillustratedbook.ui.base.item.SnackBarMessage
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultAppBar
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultButton
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultLayout
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
    val availableProducts by billingModule.purchasableProducts.collectAsStateWithLifecycle()

    GearScreen(
        gearUiState = gearUiState,
        snackBarMessage = snackBarMessage,
        availableProducts = availableProducts,
        purchaseInApp = billingModule::purchase,
        setIntervalFirstTimerSetting = gearViewModel::setIntervalFirstTimerSetting,
        setIntervalSecondTimerSetting = gearViewModel::setIntervalSecondTimerSetting,
        onNavigatePopBackStack = onNavigatePopBackStack,
        emitSnackBar = gearViewModel::emitSnackBar
    )
}
@Composable
private fun GearScreen(
    gearUiState: GearUiState,
    snackBarMessage: SnackBarMessage,
    availableProducts: List<ProductDetails>,
    purchaseInApp: (ProductDetails) -> Unit,
    setIntervalFirstTimerSetting: (Int) -> Unit,
    setIntervalSecondTimerSetting: (Int) -> Unit,
    onNavigatePopBackStack: () -> Unit,
    emitSnackBar: (SnackBarMessage) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    if (snackBarMessage.headerMessage.isNotBlank())
        LaunchedEffect(key1 = snackBarMessage) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = snackBarMessage.headerMessage,
                actionLabel = snackBarMessage.contentMessage,
                duration = SnackbarDuration.Indefinite
            )
        }

    DefaultLayout(
        topBar = {
            DefaultAppBar(
                title = "알람 설정",
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
                headerText = "첫번째",
                pickerValue = gearUiState.intervalFirstTimer,
                onPickerValueChange = { minutes -> setIntervalFirstTimerSetting(minutes) },
                onButtonClick = emitSnackBar
            )
            SettingIntervalItem(
                headerText = "두번째",
                pickerValue = gearUiState.intervalSecondTimer,
                onPickerValueChange = { minutes -> setIntervalSecondTimerSetting(minutes) },
                onButtonClick = emitSnackBar
            )
            VerticalSpacer(height = 8.dp)
            LazyColumn {
                itemsIndexed(availableProducts) { index, product ->
                    DefaultButton(
                        content = "${product.name} 하기",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                purchaseInApp(product)
                            }
                    )
                    if(index != availableProducts.lastIndex)
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
    onPickerValueChange: (Int) -> Unit,
    onButtonClick: (SnackBarMessage) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = buildAnnotatedString {
                appendBoldText(text = headerText, color = red)
                append(" 알람 간격")
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
        HorizontalSpacer(width = 16.dp)
        DefaultButton(
            content = "적용하기",
            modifier = Modifier.clickable {
                onButtonClick(
                    SnackBarMessage(
                        headerMessage = "알람 간격 설정이 완료되었습니다.",
                        contentMessage = "$headerText 알람 간격이 $pickerValue 분 전으로 설정되었습니다."
                    )
                )
            }
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
            onNavigatePopBackStack = {},
            emitSnackBar = {}
        )
    }
}