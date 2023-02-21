package com.jinproject.twomillustratedbook.ui.screen.gear

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.jinproject.twomillustratedbook.ui.base.item.SnackBarMessage
import com.jinproject.twomillustratedbook.ui.screen.compose.component.*
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.deepGray
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.red
import com.jinproject.twomillustratedbook.utils.TwomIllustratedBookPreview
import com.jinproject.twomillustratedbook.utils.appendBoldText
import com.jinproject.twomillustratedbook.utils.tu
import kotlinx.coroutines.launch

@Composable
fun GearScreen(
    gearUiState: GearUiState,
    snackBarMessage: SnackBarMessage,
    setIntervalFirstTimerSetting: (Int) -> Unit,
    setIntervalSecondTimerSetting: (Int) -> Unit,
    onNavigatePopBackStack: () -> Unit,
    emitSnackBar: (SnackBarMessage) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    if (snackBarMessage.contentMessage.isNotBlank())
        LaunchedEffect(key1 = snackBarMessage.contentMessage) {
            coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = snackBarMessage.headerMessage,
                    actionLabel = snackBarMessage.contentMessage,
                    duration = SnackbarDuration.Indefinite
                )
            }
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
        }
    }
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
            fontSize = 18.tu,
            fontWeight = FontWeight.W400
        )
        HorizontalSpacer(width = 8.dp)
        NumberPicker(
            value = pickerValue,
            onValueChange = onPickerValueChange,
            range = 0..59,
            textStyle = TextStyle(
                color = deepGray
            )
        )
        HorizontalSpacer(width = 4.dp)
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
private fun PreviewGearScreen() =
    TwomIllustratedBookPreview {
        GearScreen(
            gearUiState = GearUiState.getInitValue(),
            snackBarMessage = SnackBarMessage.getInitValues(),
            setIntervalFirstTimerSetting = {},
            setIntervalSecondTimerSetting = {},
            onNavigatePopBackStack = {},
            emitSnackBar = {}
        )
    }