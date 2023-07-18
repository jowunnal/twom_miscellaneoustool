package com.jinproject.features.alarm.component

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.features.alarm.R

@Composable
fun AlarmTopAppBar(
    onNavigateToGear: () -> Unit,
    onNavigateToOverlaySetting: () -> Unit
) {
    TopAppBar(
        title = {},
        actions = {
            IconButton(onClick = { onNavigateToGear() }) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_gear),
                    contentDescription = "GearIcon"
                )
            }
            IconButton(onClick = { onNavigateToOverlaySetting() }) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_clock),
                    contentDescription = "AlarmIcon"
                )
            }
        },
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewAlarmTopAppBar() =
    PreviewMiscellaneousToolTheme {
        AlarmTopAppBar(
            onNavigateToGear = {},
            onNavigateToOverlaySetting = {}
        )
    }