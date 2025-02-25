package com.jinproject.features.symbol.gallery.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.bar.OneButtonAppBar
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.component.text.AppBarText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R

@Composable
fun GalleryAppBar(
    title: String,
    onBackClick: () -> Unit,
    onSettingClick: () -> Unit,
) {
    OneButtonAppBar(
        buttonAlignment = Alignment.CenterStart,
        icon = R.drawable.ic_arrow_left,
        onBackClick = onBackClick
    ) {
        AppBarText(
            text = title,
            modifier = Modifier
                .align(Alignment.Center),
        )

        Image(
            modifier = Modifier
                .size(48.dp)
                .clickableAvoidingDuplication(onClick = onSettingClick)
                .align(Alignment.TopEnd)
                .padding(12.dp),
            painter = painterResource(id = R.drawable.ic_history),
            contentDescription = "Setting Icon",
        )
    }
}

@Composable
@Preview
private fun PreviewGalleryAppBar() = MiscellaneousToolTheme {
    GalleryAppBar(
        title = "",
        onBackClick = {},
        onSettingClick = {},
    )
}