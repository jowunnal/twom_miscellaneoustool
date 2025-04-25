package com.jinproject.features.alarm.watch.component

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.effect.RememberEffect
import com.jinproject.design_compose.component.text.DescriptionLargeText
import com.jinproject.design_compose.component.text.NumberInputTextField
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.alarm.watch.SettingUiState
import com.jinproject.features.alarm.watch.SettingUiStatePreviewParameter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun OverlaySetting(
    settingUiState: SettingUiState,
    context: Context = LocalContext.current,
    setTimerSetting: (fontSize: Int?, xPos: Int?, yPos: Int?, firstInterval: Int?, secondInterval: Int?) -> Unit,
) {
    val displayMetrics = context.applicationContext.resources.displayMetrics
    val fontSizeTextFieldState =
        rememberNumberInputTextField(
            initialText = settingUiState.fontSize,
            storeOverlaySetting = { number ->
                setTimerSetting(number, null, null, null, null)
            },
        )
    val xPosTextFieldState = rememberNumberInputTextField(
        initialText = settingUiState.xPos,
        storeOverlaySetting = { number ->
            setTimerSetting(null, number, null, null, null)
        },
    )
    val yPosTextFieldState = rememberNumberInputTextField(
        initialText = settingUiState.yPos,
        storeOverlaySetting = { number ->
            setTimerSetting(null, null, number, null, null)
        },
    )
    val firstInterval = rememberNumberInputTextField(
        initialText = settingUiState.firstInterval,
        storeOverlaySetting = { number ->
            setTimerSetting(null, null, null, number, null)
        },
    )
    val secondInterval =
        rememberNumberInputTextField(
            initialText = settingUiState.secondInterval,
            storeOverlaySetting = { number ->
                setTimerSetting(null, null, null, null, number)
            },
        )

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        EachSetting(
            header = stringResource(id = R.string.watch_setting_fontsize),
            textFieldState = fontSizeTextFieldState,
            maxNumber = 50,
            initialText = settingUiState.fontSize.toString(),
        )
        EachSetting(
            header = stringResource(id = R.string.watch_setting_move_x),
            textFieldState = xPosTextFieldState,
            maxNumber = displayMetrics.widthPixels,
            initialText = settingUiState.xPos.toString(),
        )
        EachSetting(
            header = stringResource(id = R.string.watch_setting_move_y),
            textFieldState = yPosTextFieldState,
            maxNumber = displayMetrics.heightPixels,
            initialText = settingUiState.yPos.toString(),
        )
        EachSetting(
            header = "${stringResource(id = R.string.first)} ${stringResource(R.string.alarm_setting_interval)}",
            textFieldState = firstInterval,
            maxNumber = 59,
            initialText = settingUiState.firstInterval.toString(),
        )
        EachSetting(
            header = "${stringResource(id = R.string.last)} ${stringResource(R.string.alarm_setting_interval)}",
            textFieldState = secondInterval,
            maxNumber = 59,
            initialText = settingUiState.secondInterval.toString(),
        )
    }
}

@OptIn(FlowPreview::class)
@Composable
fun rememberNumberInputTextField(
    initialText: Int,
    textFieldState: TextFieldState = rememberTextFieldState(initialText = initialText.toString()),
    storeOverlaySetting: (Int) -> Unit,
): TextFieldState {
    // StateFlow 의 Default 값으로 기억된 TextFieldState 에 Local 경로의 데이터가 방출된 watchUiState 의 값과 불일치 되었을 때, 동기화 시키는 부분
    RememberEffect(initialText) {
        if (initialText != 0 && textFieldState.text == "0") {
            textFieldState.edit {
                replace(0, textFieldState.text.length, initialText.toString())
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { textFieldState.text }
            .distinctUntilChanged()
            .debounce(300)
            .filter {
                it.isNotBlank() && it.toString().toIntOrNull() != null
            }
            .map { it.toString().toInt() }
            .collectLatest { number ->
                storeOverlaySetting(number)
            }
    }

    return textFieldState
}

@Composable
private fun EachSetting(
    header: String,
    textFieldState: TextFieldState,
    maxNumber: Int,
    initialText: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .shadow(5.dp, shape = RoundedCornerShape(20.dp))
            .background(
                MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp),
            )
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DescriptionLargeText(text = header, modifier = Modifier.weight(2f))
        HorizontalSpacer(12.dp)
        NumberInputTextField(
            textFieldState = textFieldState,
            modifier = Modifier.weight(1f),
            hint = stringResource(R.string.watch_setting_input_number),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            initialValue = initialText,
            maxNumber = maxNumber,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewTimerPositionSetting(
    @PreviewParameter(SettingUiStatePreviewParameter::class)
    settingUiState: SettingUiState,
) {
    MiscellaneousToolTheme {
        OverlaySetting(
            settingUiState = settingUiState,
            setTimerSetting = { _, _, _, _, _ -> }
        )
    }
}