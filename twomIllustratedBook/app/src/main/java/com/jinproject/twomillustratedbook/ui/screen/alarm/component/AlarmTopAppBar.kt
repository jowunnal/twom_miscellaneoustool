package com.jinproject.twomillustratedbook.ui.screen.alarm.component

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.black
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.white
import com.jinproject.twomillustratedbook.utils.TwomIllustratedBookPreview

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
            /*IconButton(onClick = { onNavigateToLogin() }) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_login),
                    contentDescription = "LoginIcon"
                )
            }*/
        },
        backgroundColor = white,
        contentColor = black
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewAlarmTopAppBar() =
    TwomIllustratedBookPreview {
        AlarmTopAppBar(
            onNavigateToGear = {},
            onNavigateToOverlaySetting = {}
        )
    }