package com.jinproject.features.alarm.watch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.bar.BackButtonTitleAppBar
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.alarm.watch.component.OverlaySetting
import com.jinproject.features.alarm.watch.component.TimeStatusSetting

@Composable
fun AlarmSettingScreen(
    alarmSettingViewModel: AlarmSettingViewModel = hiltViewModel(),
    onNavigatePopBackStack: () -> Unit
) {
    val uiState by alarmSettingViewModel.uiState.collectAsStateWithLifecycle()

    AlarmSettingScreen(
        uiState = uiState,
        setTimerSetting = alarmSettingViewModel::setTimerSetting,
        onNavigatePopBackStack = onNavigatePopBackStack,
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