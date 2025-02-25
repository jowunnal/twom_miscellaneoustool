package com.jinproject.features.symbol.guildmark.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.component.text.FooterText
import com.jinproject.design_compose.component.HorizontalWeightSpacer
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R

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
        FooterText(text = stringResource(id = R.string.very_high))
        HorizontalWeightSpacer(float = 1f)
        DescriptionSmallText(text = stringResource(id = R.string.color_precision))
        HorizontalWeightSpacer(float = 1f)
        FooterText(text = stringResource(id = R.string.very_low))
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