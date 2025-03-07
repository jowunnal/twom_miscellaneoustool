package com.jinproject.features.alarm.gear

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.chargemap.compose.numberpicker.NumberPicker
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.button.TextButton
import com.jinproject.design_compose.component.text.DescriptionAnnotatedLargeText
import com.jinproject.design_compose.theme.MiscellaneousToolColor.Companion.red
import com.jinproject.design_ui.R
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.utils.appendBoldText


@Composable
fun GearScreen(
    gearViewModel: GearViewModel = hiltViewModel(),
    onNavigatePopBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit
) {
    val gearUiState by gearViewModel.uiState.collectAsStateWithLifecycle()

    GearScreen(
        gearUiState = gearUiState,
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
    context: Context = LocalContext.current,
    setIntervalFirstTimerSetting: (Int) -> Unit,
    setIntervalSecondTimerSetting: (Int) -> Unit,
    setIntervalTimerSetting: () -> Unit,
    onNavigatePopBackStack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit
) {
    DefaultLayout(
        topBar = {
            BackButtonTitleAppBar(
                onBackClick = onNavigatePopBackStack,
                title = stringResource(id = R.string.alarm_setting),
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
            VerticalSpacer(height = 20.dp)
            TextButton(
                text = stringResource(id = R.string.apply_do),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                onClick = {
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
            )

            VerticalSpacer(height = 30.dp)
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
        DescriptionAnnotatedLargeText(text = buildAnnotatedString {
            appendBoldText(text = headerText, color = red.color)
            append(" ${stringResource(id = R.string.alarm_setting_interval)}")
        })
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
            setIntervalFirstTimerSetting = {},
            setIntervalSecondTimerSetting = {},
            setIntervalTimerSetting = {},
            onNavigatePopBackStack = {},
            showSnackBar = {}
        )
    }
}