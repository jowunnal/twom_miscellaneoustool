package com.jinproject.features.watch.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.component.DefaultButton
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.theme.Typography
import com.jinproject.features.watch.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TimerBottomSheetContent(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    selectedMonsterName: String,
    onCloseBottomSheet: () -> Unit,
    setSelectedMonsterOtaToTrue: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_handle_bar),
                contentDescription = "HandleBar",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.scrim)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick =
                {
                    coroutineScope.launch {
                        onCloseBottomSheet()
                    }
                },
                modifier = Modifier
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_x),
                    contentDescription = "ExitIcon",
                    tint = MaterialTheme.colorScheme.scrim
                )
            }
        }
        Text(
            text = selectedMonsterName,
            style = Typography.headlineMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        VerticalSpacer(height = 16.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.watch_bottomsheet_title),
                style = Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        VerticalSpacer(height = 16.dp)

        Row() {
            DefaultButton(content = stringResource(id = R.string.register_do),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        setSelectedMonsterOtaToTrue(1)
                        onCloseBottomSheet()
                    }
            )
            HorizontalSpacer(width = 8.dp)
            DefaultButton(content = stringResource(id = R.string.delete_do),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        setSelectedMonsterOtaToTrue(0)
                        onCloseBottomSheet()
                    }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewTimerBottomSheetContent() =
    PreviewMiscellaneousToolTheme {
        TimerBottomSheetContent(
            selectedMonsterName = "은둔자",
            onCloseBottomSheet = {},
            setSelectedMonsterOtaToTrue = {}
        )
    }