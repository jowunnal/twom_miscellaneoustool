package com.miscellaneoustool.app.ui.screen.watch.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miscellaneoustool.app.R
import com.miscellaneoustool.app.ui.screen.compose.component.DefaultButton
import com.miscellaneoustool.app.ui.screen.compose.component.HorizontalSpacer
import com.miscellaneoustool.app.ui.screen.compose.component.VerticalSpacer
import com.miscellaneoustool.app.ui.screen.compose.theme.black
import com.miscellaneoustool.app.ui.screen.compose.theme.deepGray
import com.miscellaneoustool.app.ui.screen.compose.theme.gray
import com.miscellaneoustool.app.utils.TwomIllustratedBookPreview
import com.miscellaneoustool.app.utils.tu
import kotlinx.coroutines.launch

@Composable
fun TimerBottomSheetContent(
    selectedMonsterName: String,
    onCloseBottomSheet: () -> Unit,
    setSelectedMonsterOtaToTrue: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_handle_bar),
                contentDescription = "HandleBar"
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
                    .height(24.dp)
                    .width(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_x),
                    contentDescription = "ExitIcon",
                    tint = gray
                )
            }
        }
        Text(
            text = selectedMonsterName,
            fontSize = 18.tu,
            fontWeight = FontWeight.ExtraBold,
            color = deepGray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        VerticalSpacer(height = 16.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "현재시간 항상 보기에 등록 하시겠습니까?",
                fontSize = 18.tu,
                fontWeight = FontWeight.ExtraBold,
                color = black
            )
        }

        VerticalSpacer(height = 16.dp)

        Row() {
            DefaultButton(content = "등록하기", modifier = Modifier
                .weight(1f)
                .clickable {
                    setSelectedMonsterOtaToTrue(1)
                    onCloseBottomSheet()
                }
            )
            HorizontalSpacer(width = 8.dp)
            DefaultButton(content = "제거하기", modifier = Modifier
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
    TwomIllustratedBookPreview {
        TimerBottomSheetContent(
            selectedMonsterName = "은둔자",
            onCloseBottomSheet = {},
            setSelectedMonsterOtaToTrue = {}
        )
    }