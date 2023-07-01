package com.miscellaneoustool.app.ui.screen.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miscellaneoustool.app.ui.screen.compose.theme.Typography
import com.miscellaneoustool.app.utils.TwomIllustratedBookPreview

@Composable
fun DefaultButton(
    content: String,
    modifier: Modifier = Modifier,
    style: TextStyle = Typography.headlineSmall,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
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
            style = style,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview()
@Composable
private fun PreviewDefaultButton() =
    TwomIllustratedBookPreview {
        DefaultButton(content = "버튼")
    }