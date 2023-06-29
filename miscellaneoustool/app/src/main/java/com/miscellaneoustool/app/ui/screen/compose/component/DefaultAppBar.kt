package com.miscellaneoustool.app.ui.screen.compose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miscellaneoustool.app.R
import com.miscellaneoustool.app.ui.screen.compose.theme.black
import com.miscellaneoustool.app.ui.screen.compose.theme.white
import com.miscellaneoustool.app.utils.TwomIllustratedBookPreview
import com.miscellaneoustool.app.utils.tu

@Composable
fun DefaultAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onBackClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(white)
            .padding(horizontal = 16.dp),
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(
                    onClick = onBackClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false, radius = 18.dp)
                ),
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = "back"
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = title,
            color = black,
            fontSize = 16.tu,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun PreviewDefaultAppBar() = TwomIllustratedBookPreview {
    DefaultAppBar(
        title = "앱바 타이틀",
        onBackClick = {}
    )
}