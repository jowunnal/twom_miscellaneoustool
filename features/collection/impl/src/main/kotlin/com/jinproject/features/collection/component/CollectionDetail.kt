package com.jinproject.features.collection.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.jinproject.design_compose.component.CoilBasicImage
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
import com.jinproject.design_compose.utils.clearFocusIfKeyboardActive
import com.jinproject.design_ui.R
import com.jinproject.features.collection.CollectionEvent
import com.jinproject.features.collection.CollectionUiStatePreviewParameter
import com.jinproject.features.collection.model.CollectionUiState
import com.jinproject.features.collection.model.Equipment
import com.jinproject.features.collection.model.ItemCollection
import com.jinproject.features.collection.model.MiscellaneousItem
import com.jinproject.features.core.utils.AssetConfig
import com.jinproject.features.core.utils.getImageDataFromAsset
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest

@Composable
internal fun CollectionDetail(
    configuration: Configuration = LocalConfiguration.current,
    collection: ItemCollection,
    dispatchEvent: (CollectionEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val padding = MiscellanousToolPaddingValues(
        horizontal = 12.dp,
        vertical = 16.dp,
    )
    val itemWidthDp = when (currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.EXPANDED, WindowWidthSizeClass.MEDIUM ->
            (configuration.screenWidthDp.dp - padding.calculateHorizontalPadding()) / 4

        else ->
            (configuration.screenWidthDp.dp - padding.calculateHorizontalPadding()) / 2
    }

    val prices = remember(collection.id) {
        mutableStateListOf<String>(*collection.items.map { it.price.toString() }.toTypedArray())
    }

    val calculatedItemPrices by remember {
        derivedStateOf {
            prices.mapIndexed { idx, price ->
                if (price.isNotBlank())
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
            .focusable()
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
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    DescriptionMediumText(
                        text = stringResource(id = R.string.item),
                    )
                    collection.items.forEach { item ->
                        key(item.name) {
                            Row(
                                modifier = Modifier.height(40.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                if (item.imageName.isNotBlank())
                                    CoilBasicImage(
                                        data = getImageDataFromAsset(
                                            context = LocalContext.current,
                                            prefix = AssetConfig.ITEM_PATH_PREFIX,
                                            imageName = item.imageName
                                        )
                                    )
                                else
                                    Spacer(Modifier.size(10.dp))
                                DescriptionSmallText(
                                    text = item.name,
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    DescriptionMediumText(
                        text = stringResource(id = R.string.price),
                    )
                    collection.items.forEachIndexed { idx, item ->
                        key(item.name) {
                            PriceTextField(
                                modifier = Modifier.height(40.dp),
                                price = item.price.toString(),
                                updatePrice = { price ->
                                    prices[idx] = price
                                },
                            )
                        }
                    }
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
                        text = "${item.name} ${if (item is Equipment && item.enchantNumber > 0) "(+${item.enchantNumber})" else ""} * ${if (item.count > 1) item.count else ""}",
                        modifier = Modifier.width(itemWidthDp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                    DescriptionSmallText(
                        text = "${
                            kotlin.runCatching { calculatedItemPrices[idx] }
                                .getOrElse { collection.items[idx].price }
                        } ${
                            stringResource(
                                id = R.string.gold
                            )
                        }",
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
            TextButton(
                text = stringResource(id = R.string.delete_do),
                modifier = Modifier
                    .width(itemWidthDp)
                    .padding(horizontal = 12.dp),
                onClick = {
                    dispatchEvent(CollectionEvent.AddFilteringCollectionId(collection.id))
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
                                price = newPrice,
                                imageName = item.imageName
                            )
                        }
                    }

                    dispatchEvent(
                        CollectionEvent.UpdateItemsPrice(newItems)
                    )
                },
            )
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
internal fun PriceTextField(
    modifier: Modifier = Modifier,
    price: String,
    textFieldState: TextFieldState = rememberTextFieldState(initialText = price).apply {
        edit {
            placeCursorAtEnd()
        }
    },
    updatePrice: (String) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        snapshotFlow {
            textFieldState.text
        }.mapLatest {
            it.toString()
        }.collectLatest {
            updatePrice(it)
            textFieldState.edit { placeCursorBeforeCharAt(it.length) }
        }
    }

    val textGold = stringResource(id = R.string.gold)

    DefaultTextField(
        modifier = modifier
            .onFocusChanged {
                textFieldState.edit {
                    if (it.hasFocus)
                        delete(start = 0, end = length)
                    else {
                        if (length == 0)
                            insert(0, price)
                    }
                }
            },
        textFieldState = textFieldState,
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
    )
}