package com.jinproject.design_compose.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.PreviewMiscellaneousToolTheme
import com.jinproject.design_compose.component.button.DefaultIconButton
import com.jinproject.design_compose.component.text.AppBarText
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.theme.Typography
import com.jinproject.design_ui.R

@Composable
fun OneButtonAppBar(
    buttonAlignment: Alignment,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    DefaultAppBar(
        content = {
            DefaultIconButton(
                modifier = Modifier
                    .align(buttonAlignment),
                icon = icon,
                onClick = onClick,
                iconTint = MaterialTheme.colorScheme.onSurface,
                interactionSource = remember { MutableInteractionSource() }
            )
            content()
        }
    )
}

@Composable
fun BackButtonTitleAppBar(
    onClick: () -> Unit,
    title: String
) {
    DefaultAppBar(
        content = {
            DefaultIconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                icon = R.drawable.ic_arrow_left,
                onClick = onClick,
                iconTint = MaterialTheme.colorScheme.onSurface,
                interactionSource = remember { MutableInteractionSource() }
            )
            AppBarText(text = title, modifier = Modifier.align(Alignment.Center))
        }
    )
}

@Composable
fun DefaultAppBar(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RectangleShape, clip = false)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        content()
    }
}

@Composable
fun DefaultRowScopeAppBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RectangleShape, clip = false)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@Composable
fun BackButtonRowScopeAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    DefaultRowScopeAppBar(modifier = modifier) {
        DefaultIconButton(
            modifier = Modifier,
            icon = R.drawable.ic_arrow_left,
            onClick = onBackClick,
            iconTint = MaterialTheme.colorScheme.onSurface,
            interactionSource = remember { MutableInteractionSource() }
        )
        content()
    }
}

@Composable
fun BackButtonSearchAppBar(
    textFieldState: TextFieldState,

) {
    BackButtonRowScopeAppBar(
        onBackClick = {}
    ) {
        BasicTextField(
            modifier = Modifier.padding(end = 12.dp),
            state = textFieldState,
            decorator = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                ) {
                    if(textFieldState.text.isEmpty())
                        DescriptionSmallText(
                            text = "입력하쇼",
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    innerTextField()
                }
            },
        )
    }
}

@Composable
fun DefaultAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onBackClick: () -> Unit,
) {
    //TODO 없애야함
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(4.dp, RectangleShape, clip = false)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(
                    onClick = onBackClick,
                ),
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = "back",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = title,
            style = Typography.headlineSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun PreviewDefaultAppBar() =
    PreviewMiscellaneousToolTheme {
        DefaultAppBar(
            title = "앱바 타이틀",
            onBackClick = {}
        )
    }