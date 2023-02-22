package com.jinproject.twomillustratedbook.ui.screen.watch.component

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.jinproject.twomillustratedbook.ui.Service.OverlayService
import com.jinproject.twomillustratedbook.ui.screen.compose.component.DefaultButton
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.black
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.deepGray
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.primary
import com.jinproject.twomillustratedbook.utils.TwomIllustratedBookPreview
import com.jinproject.twomillustratedbook.utils.tu

@Composable
fun TimerFontSizeSetting(
    fontSize: Int,
    setFontSize: (Int) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "폰트 크기 조절",
            fontSize = 16.tu,
            fontWeight = FontWeight.W700,
            color = deepGray,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        NumberPicker(
            value = fontSize,
            onValueChange = { value -> setFontSize(value) },
            range = 1..32,
            textStyle = TextStyle(color = deepGray),
            dividersColor = primary,
            modifier = Modifier.align(Alignment.Center)
        )
        DefaultButton(
            content = "설정하기",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                context.startForegroundService(
                    Intent(
                        context, OverlayService::class.java
                    ).apply {
                        putExtra("fontSize",fontSize)
                    }
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimerFontSizeSetting() {
    TwomIllustratedBookPreview {
        TimerFontSizeSetting(
            fontSize = 14,
            setFontSize = {}
        )
    }
}