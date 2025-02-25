package com.jinproject.features.alarm.watch.component

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.chargemap.compose.numberpicker.NumberPicker
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_compose.theme.Typography
import com.jinproject.design_ui.R

@Composable
fun OverlaySetting(
    fontSize: Int,
    xPos: Int,
    yPos: Int,
    setFontSize: (Int) -> Unit,
    setXPos: (Int) -> Unit,
    setYPos: (Int) -> Unit,
    context: Context = LocalContext.current
) {
    val displayMetrics = context.applicationContext.resources.displayMetrics
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NumberPicker(
            title = stringResource(id = R.string.watch_setting_fontsize),
            number = fontSize,
            setNumber = setFontSize,
            range = 1..32
        )
        Spacer(modifier = Modifier.weight(1f))
        NumberPicker(
            title = stringResource(id = R.string.watch_setting_move_x),
            number = xPos,
            setNumber = setXPos,
            range = -(displayMetrics.widthPixels / 2)..(displayMetrics.widthPixels / 2) step 5
        )
        Spacer(modifier = Modifier.weight(1f))
        NumberPicker(
            title = stringResource(id = R.string.watch_setting_move_y),
            number = yPos,
            setNumber = setYPos,
            range = 0..displayMetrics.heightPixels / 2 step 5
        )
    }
}

@Composable
private fun NumberPicker(
    title: String,
    number: Int,
    range: IntProgression,
    setNumber: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = Typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        NumberPicker(
            value = number,
            onValueChange = { value -> setNumber(value) },
            range = range,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.outline),
            dividersColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
@Preview
private fun PreviewTimerPositionSetting() =
    MiscellaneousToolTheme {
        OverlaySetting(
            fontSize = 0,
            xPos = 0,
            yPos = 0,
            setFontSize = {},
            setXPos = {},
            setYPos = {}
        )
    }