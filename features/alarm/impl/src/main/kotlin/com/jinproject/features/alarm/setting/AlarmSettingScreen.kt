package com.jinproject.features.alarm.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.alarm.setting.component.OverlaySetting
import com.jinproject.features.alarm.setting.component.TimeStatusSetting
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.compose.LocalNavigator
import com.jinproject.features.core.compose.LocalShowSnackbar

@Composable
fun AlarmSettingScreen(
    alarmSettingViewModel: AlarmSettingViewModel = hiltViewModel(),
) {
    val navigator = LocalNavigator.current
    val uiState by alarmSettingViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val showSnackBarMessage = LocalShowSnackbar.current

    AlarmSettingScreen(
        uiState = uiState,
        setTimerSetting = { fontSize, xPos, yPos, first, second ->
            alarmSettingViewModel.setTimerSetting(
                fontSize = fontSize,
                xPos = xPos,
                yPos = yPos,
                firstInterval = first,
                secondInterval = second,
                showSnackBarMessage = {
                    showSnackBarMessage(
                        SnackBarMessage(
                            headerMessage = context.getString(R.string.alarm_setting_interval_failure),
                            contentMessage = context.getString(R.string.alarm_setting_interval_failure_reason),
                        )
                    )
                }
            )
        },
        onNavigatePopBackStack = { navigator.goBack() },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AlarmSettingScreen(
    uiState: SettingUiState,
    setTimerSetting: (Int?, Int?, Int?, Int?, Int?) -> Unit,
    onNavigatePopBackStack: () -> Unit,
) {
    val scrollState = rememberScrollState()

    DefaultLayout(
        topBar = {
            BackButtonTitleAppBar(
                onBackClick = onNavigatePopBackStack,
                title = stringResource(id = R.string.watch_appbar_title),
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(scrollState)
        ) {
            TimeStatusSetting()

            VerticalSpacer(height = 60.dp)

            OverlaySetting(
                settingUiState = uiState,
                setTimerSetting = setTimerSetting
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PreviewAlarmSettingScreen(
    @PreviewParameter(SettingUiStatePreviewParameter::class)
    settingUiState: SettingUiState,
) {
    PreviewMiscellaneousToolTheme {
        AlarmSettingScreen(
            uiState = settingUiState,
            setTimerSetting = { _, _, _, _, _ -> },
            onNavigatePopBackStack = {},
        )
    }
}
