package com.jinproject.features.collection.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.jinproject.design_compose.clearFocusIfKeyboardActive
import com.jinproject.design_compose.component.HorizontalDivider
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.bar.BackButtonRowScopeAppBar
import com.jinproject.design_compose.component.button.TextButton
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.component.paddingvalues.MiscellanousToolPaddingValues
import com.jinproject.design_compose.component.text.DefaultTextField
import com.jinproject.design_compose.component.text.DescriptionMediumText
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.component.text.TenThousandSeparatorOutputTransformation
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.collection.CollectionEvent
import com.jinproject.features.collection.CollectionUiStatePreviewParameter
import com.jinproject.features.collection.model.CollectionUiState
import com.jinproject.features.collection.model.Equipment
import com.jinproject.features.collection.model.ItemCollection
import com.jinproject.features.collection.model.MiscellaneousItem
import com.jinproject.features.core.base.item.SnackBarMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest

@Composable
internal fun CollectionDetail(
    configuration: Configuration = LocalConfiguration.current,
    collection: ItemCollection,
    dispatchEvent: (CollectionEvent) -> Unit,
    onNavigateBack: () -> Unit,
    showSnackBar: (SnackBarMessage) -> Unit,
) {
    val padding = MiscellanousToolPaddingValues(
        horizontal = 12.dp,
        vertical = 16.dp,
    )
    val itemWidthDp = when(currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.EXPANDED, WindowWidthSizeClass.MEDIUM ->
            (configuration.screenWidthDp.dp - padding.calculateHorizontalPadding()) / 4

        else ->
            (configuration.screenWidthDp.dp - padding.calculateHorizontalPadding()) / 2
    }

    val prices = remember(collection.items) {
        mutableStateListOf<String>()
    }.apply {
        addAll(collection.items.map { it.price.toString() })
    }

    val calculatedItemPrices by remember {
        derivedStateOf {
            prices.mapIndexed { idx, price ->
                if(price.isNotBlank())
                    collection.items[idx].count * price.toLong()
                else
                    0L
            }
        }
    }

    DefaultLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clearFocusIfKeyboardActive(),
        topBar = {
            BackButtonRowScopeAppBar(
                onBackClick = onNavigateBack,
            )
        },
        contentPaddingValues = padding,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                DescriptionMediumText(
                    text = stringResource(id = R.string.item),
                    modifier = Modifier.width(itemWidthDp),
                )
                DescriptionMediumText(
                    text = stringResource(id = R.string.price),
                    modifier = Modifier.width(itemWidthDp),
                )
            }
            collection.items.forEachIndexed { idx, item ->
                key(item.name) {
                    ItemWithPrice(
                        name = item.name,
                        itemWidthDp = itemWidthDp,
                        price = item.price.toString(),
                        updatePrice = { price ->
                            prices[idx] = price
                        }
                    )
                }
            }
            VerticalSpacer(height = 30.dp)
            DescriptionMediumText(
                text = stringResource(id = R.string.stat),
                modifier = Modifier,
            )
            VerticalSpacer(height = 5.dp)
            collection.stats.forEach { stat ->
                DescriptionSmallText(
                    text = "${stat.key} : ${stat.value}",
                    modifier = Modifier
                        .width(itemWidthDp)
                        .padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }
            VerticalSpacer(height = 30.dp)
            DescriptionMediumText(
                text = stringResource(id = R.string.total),
                modifier = Modifier,
            )
            VerticalSpacer(height = 5.dp)
            collection.items.forEachIndexed { idx, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    DescriptionSmallText(
                        text = "${item.name} * ${item.count}",
                        modifier = Modifier.width(itemWidthDp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                    DescriptionSmallText(
                        text = "${calculatedItemPrices[idx]} ${stringResource(id = R.string.gold)}",
                        modifier = Modifier.width(itemWidthDp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )

                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = itemWidthDp, top = 2.dp, bottom = 2.dp)
            ) {
                DescriptionSmallText(
                    text = "${calculatedItemPrices.sum()} ${stringResource(id = R.string.gold)}",
                    modifier = Modifier.width(itemWidthDp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            val deleteSnackBarMessage =
                stringResource(id = R.string.message_success_removed)
            val applySnackBarMessage =
                stringResource(id = R.string.message_success_applied)

            TextButton(
                text = stringResource(id = R.string.delete_do),
                modifier = Modifier
                    .width(itemWidthDp)
                    .padding(horizontal = 12.dp),
                onClick = {
                    dispatchEvent(CollectionEvent.AddFilteringCollectionId(collection.id))
                    onNavigateBack()
                    showSnackBar(SnackBarMessage(headerMessage = deleteSnackBarMessage))
                },
            )
            TextButton(
                text = stringResource(id = R.string.apply_do),
                modifier = Modifier
                    .width(itemWidthDp)
                    .padding(horizontal = 12.dp),
                onClick = {
                    val newItems = collection.items.mapIndexed { idx, item ->
                        val newPrice = if (prices[idx].isNotBlank()) prices[idx].toLong() else 0L

                        when (item) {
                            is Equipment -> item.copy(price = newPrice)
                            is MiscellaneousItem -> item.copy(price = newPrice)
                            else -> MiscellaneousItem(
                                name = item.name,
                                count = item.count,
                                price = newPrice
                            )
                        }
                    }

                    dispatchEvent(
                        CollectionEvent.UpdateItemsPrice(newItems)
                    )
                    onNavigateBack()
                    showSnackBar(SnackBarMessage(headerMessage = applySnackBarMessage))
                },
            )
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
internal fun ItemWithPrice(
    name: String,
    itemWidthDp: Dp,
    price: String,
    textFiledState: TextFieldState = rememberTextFieldState(initialText = price).apply {
        edit {
            placeCursorAtEnd()
        }
    },
    updatePrice: (String) -> Unit,
) {

    LaunchedEffect(key1 = Unit) {
        snapshotFlow {
            textFiledState.text
        }.mapLatest {
            it.toString()
        }.collectLatest {
            updatePrice(it)
            textFiledState.edit { placeCursorBeforeCharAt(it.length) }
        }
    }

    val textGold = stringResource(id = com.jinproject.design_ui.R.string.gold)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DescriptionSmallText(
            text = name,
            modifier = Modifier.width(itemWidthDp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        DefaultTextField(
            modifier = Modifier
                .onFocusChanged {
                    textFiledState.edit {
                        if (it.hasFocus)
                            delete(start = 0, end = length)
                        else {
                            if (length == 0)
                                insert(0, price)
                        }
                    }
                },
            textFieldState = textFiledState,
            textStyle = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface),
            outputTransformation = TenThousandSeparatorOutputTransformation(
                suffix = {
                    append(" $textGold")
                }
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            inputTransformation = InputTransformation.maxLength(10),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewCollectionDetail(
    @PreviewParameter(CollectionUiStatePreviewParameter::class)
    collectionUiState: CollectionUiState
) = MiscellaneousToolTheme {
    CollectionDetail(
        collection = collectionUiState.itemCollections.first(),
        dispatchEvent = {},
        onNavigateBack = {},
        showSnackBar = {},
    )
}