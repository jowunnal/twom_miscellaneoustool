package com.jinproject.features.symbol.guildmark.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.DescriptionText
import com.jinproject.design_compose.component.FooterText
import com.jinproject.design_compose.component.HorizontalWeightSpacer
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.theme.MiscellaneousToolTheme

@Composable
internal fun ColorSlider(
    slider: Float,
    setSlider: (Float) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
    ) {
        FooterText(text = "매우 높음")
        HorizontalWeightSpacer(float = 1f)
        DescriptionText(text = "색상 정밀도")
        HorizontalWeightSpacer(float = 1f)
        FooterText(text = "매우 낮음")
    }
    VerticalSpacer(height = 10.dp)
    Slider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        value = slider,
        onValueChange = { pos -> setSlider(pos) },
        valueRange = 0f.rangeTo(30f),
        steps = 30
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewGuildMarkSlider() = MiscellaneousToolTheme {
    ColorSlider(slider = 0f, setSlider = {})
}