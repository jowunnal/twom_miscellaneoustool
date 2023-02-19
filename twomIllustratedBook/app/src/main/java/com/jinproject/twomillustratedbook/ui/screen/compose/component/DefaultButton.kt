package com.jinproject.twomillustratedbook.ui.screen.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.*
import com.jinproject.twomillustratedbook.utils.TwomIllustratedBookPreview
import com.jinproject.twomillustratedbook.utils.tu

@Composable
fun DefaultButton(
    content: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 18,
    backgroundColor: Color = primary,
    contentPaddingValues: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(backgroundColor)
            .padding(contentPaddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = content,
            fontSize = fontSize.tu,
            fontWeight = FontWeight.W700,
            color = white
        )
    }
}

@Preview()
@Composable
private fun PreviewDefaultButton() =
    TwomIllustratedBookPreview {
        DefaultButton(content = "버튼")
    }