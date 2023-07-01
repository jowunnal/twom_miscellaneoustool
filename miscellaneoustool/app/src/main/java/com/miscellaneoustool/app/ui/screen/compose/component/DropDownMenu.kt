package com.miscellaneoustool.app.ui.screen.compose.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miscellaneoustool.app.R
import com.miscellaneoustool.app.ui.screen.compose.theme.Typography
import com.miscellaneoustool.app.utils.TwomIllustratedBookPreview

@Composable
fun DropDownMenuCustom(
    @DrawableRes iconHeader: Int? = null,
    @DrawableRes iconTail: Int? = null,
    label: String,
    text: String,
    items: List<String>,
    setTextChanged: (String) -> Unit
) {
    val dropDownExpandedState = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .clickable {
                dropDownExpandedState.value = !dropDownExpandedState.value
            }
    ) {
        Text(
            text = label,
            style = Typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        VerticalSpacer(height = 1.dp)
        Row(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.scrim, RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            iconHeader?.let {
                Icon(
                    painter = painterResource(id = iconHeader),
                    contentDescription = "DropDownMenuIconHeader"
                )
                HorizontalSpacer(width = 12.dp)
            }

            Text(
                text = text,
                style = Typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            iconTail?.let {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = iconTail),
                        contentDescription = "DropDownMenuIconTail",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        DropdownMenu(
            expanded = dropDownExpandedState.value,
            onDismissRequest = { dropDownExpandedState.value = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            items.forEach {
                androidx.compose.material.DropdownMenuItem(
                    onClick = {
                        setTextChanged(it)
                        dropDownExpandedState.value = false
                    }
                ) {
                    Text(text = it)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewDropDownMenuCustom() {
    TwomIllustratedBookPreview {
        DropDownMenuCustom(
            iconHeader = R.drawable.icon_home,
            iconTail = R.drawable.icon_alarm,
            label = "라벨텍스트",
            text = "컨텐트 텍스트",
            setTextChanged = {},
            items = emptyList()
        )
    }
}