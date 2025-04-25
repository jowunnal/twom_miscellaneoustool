package com.jinproject.features.alarm.setting

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class SettingUiStatePreviewParameter : PreviewParameterProvider<SettingUiState> {
    override val values: Sequence<SettingUiState> = sequenceOf(
        SettingUiState(
            fontSize = 14,
            xPos = 100,
            yPos = 150,
            firstInterval = 5,
            secondInterval = 0,
        )
    )
}