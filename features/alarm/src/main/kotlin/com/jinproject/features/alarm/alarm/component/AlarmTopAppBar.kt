package com.jinproject.features.alarm.alarm.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.jinproject.design_compose.component.bar.DefaultAppBar
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_ui.R

@Composable
fun AlarmTopAppBar(
    onNavigateToOverlaySetting: () -> Unit
) {
    DefaultAppBar(
        content = {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                IconButton(onClick = { onNavigateToOverlaySetting() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_gear),
                        contentDescription = "GearIcon",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewAlarmTopAppBar() =
    PreviewMiscellaneousToolTheme {
        AlarmTopAppBar(
            onNavigateToOverlaySetting = {}
        )
    }