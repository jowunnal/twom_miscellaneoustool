package com.jinproject.features.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.design_compose.component.HorizontalDivider
import com.jinproject.design_compose.component.HorizontalSpacer
import com.jinproject.design_compose.component.HorizontalWeightSpacer
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.button.DefaultIconButton
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.component.button.combinedClickableAvoidingDuplication
import com.jinproject.design_compose.component.image.DefaultPainterImage
import com.jinproject.design_compose.component.layout.DefaultLayout
import com.jinproject.design_compose.component.paddingvalues.MiscellanousToolPaddingValues
import com.jinproject.design_compose.component.text.DescriptionAnnotatedSmallText
import com.jinproject.design_compose.component.text.DescriptionLargeText
import com.jinproject.design_compose.component.text.DescriptionSmallText
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.collection.model.ItemCollection
import com.jinproject.features.core.utils.appendBoldText
import com.jinproject.features.droplist.component.DropListMonster
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDropList: (String?) -> Unit,
    navigateToCollection: (Int?) -> Unit,
    navigateToAlarm: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        navigateToDropList = navigateToDropList,
        navigateToCollection = navigateToCollection,
        navigateToAlarm = navigateToAlarm,
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    context: Context = LocalContext.current,
    navigateToDropList: (String?) -> Unit,
    navigateToCollection: (Int?) -> Unit,
    navigateToAlarm: () -> Unit,
) {
    DefaultLayout(
        modifier = Modifier,
        contentPaddingValues = MiscellanousToolPaddingValues(horizontal = 12.dp),
        verticalScrollable = true,
    ) {
        VerticalSpacer(8.dp)
        HomeMenu(
            header = stringResource(id = R.string.home_droplist),
            onClickHeader = {
                navigateToDropList(null)
            },
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentPadding = MiscellanousToolPaddingValues(
                    vertical = 16.dp,
                    horizontal = 12.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.maps, key = { mapState -> mapState.name }) { mapState ->
                    DropListMonster(
                        mapState = mapState,
                        itemWidth = 100.dp,
                        onClickItem = { map ->
                            navigateToDropList(map.name)
                        },
                    )

                }
            }
        }

        VerticalSpacer(height = 40.dp)

        HomeMenu(
            header = stringResource(id = R.string.home_collection),
            onClickHeader = {
                navigateToCollection(null)
            },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentPadding = MiscellanousToolPaddingValues(
                    vertical = 16.dp,
                    horizontal = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.collections, key = { collection -> collection.id }) { collection ->
                    CollectionItem(
                        collection = collection,
                        navigateToCollectionDetail = { id ->
                            navigateToCollection(id)
                        },
                    )
                }
            }
        }

        VerticalSpacer(height = 40.dp)

        HomeMenu(
            header = stringResource(id = R.string.alarm_present_bosslist),
            onClickHeader = navigateToAlarm,
        ) {
            if (uiState.bossTimer.isEmpty())
                DescriptionSmallText(
                    text = stringResource(id = R.string.empty),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                        .padding(vertical = 4.dp),
                )
            else
                uiState.bossTimer.forEach { timer ->
                    key(timer.name) {
                        DescriptionAnnotatedSmallText(
                            text = buildAnnotatedString {
                                appendBoldText(
                                    text = timer.name,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                append(" ${timer.time.format(DateTimeFormatter.ofPattern("MM/dd"))}  (")
                                appendBoldText(
                                    text = timer.time.dayOfWeek.getDisplayName(
                                        TextStyle.SHORT,
                                        context.resources.configuration.locales[0]
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                append(") ${timer.time.format(DateTimeFormatter.ofPattern("a"))} ")
                                appendBoldText(
                                    text = timer.time.hour.toString(),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                append(
                                    "${
                                        context.doOnLocaleLanguage(
                                            onKo = stringResource(id = R.string.hour),
                                            onElse = " :"
                                        )
                                    } "
                                )
                                appendBoldText(
                                    text = timer.time.minute.toString(),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                append(
                                    "${
                                        context.doOnLocaleLanguage(
                                            onKo = stringResource(id = R.string.minute),
                                            onElse = " :"
                                        )
                                    } "
                                )
                                appendBoldText(
                                    text = timer.time.second.toString(),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                append(stringResource(id = R.string.second))
                            },
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth()
                                .padding(vertical = 4.dp),
                        )
                    }
                }
        }
    }
}

@Composable
private fun HomeMenu(
    header: String,
    onClickHeader: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(20.dp))
            .padding(vertical = 16.dp, horizontal = 12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickableAvoidingDuplication { onClickHeader() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DescriptionLargeText(text = header)
            HorizontalWeightSpacer(float = 1f)
            DefaultIconButton(
                icon = R.drawable.ic_arrow_right_small,
                onClick = onClickHeader,
                iconSize = 24.dp
            )
        }
        VerticalSpacer(height = 8.dp)
        HorizontalDivider()
        VerticalSpacer(height = 5.dp)
        content()
    }
}

@Composable
private fun CollectionItem(
    modifier: Modifier = Modifier,
    collection: ItemCollection,
    navigateToCollectionDetail: (Int) -> Unit,
) {
    val stat =
        remember(collection.stats) {
            collection.stats.entries.joinToString("\n") { entry ->
                if (entry.key.last() == '%')
                    "${entry.key.dropLast(1)} ${entry.value}%"
                else
                    "${entry.key} ${entry.value}"
            }
        }
    val item = remember(collection.items) {
        collection.items.joinToString("\n") { item -> "${item.name} * ${item.count}" }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(5.dp, shape = RoundedCornerShape(20.dp))
            .background(
                MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp)
            )
            .combinedClickableAvoidingDuplication(
                onClick = {
                    navigateToCollectionDetail(collection.id)
                },
            )
            .padding(vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val verticalPadding = 10.dp
            DescriptionSmallText(
                text = item,
                modifier = Modifier
                    .weight(1f),
            )
            HorizontalSpacer(width = 5.dp)
            DefaultPainterImage(
                resId = com.jinproject.design_ui.R.drawable.ic_arrow_right_long,
                contentDescription = "Right Long Arrow",
            )
            HorizontalSpacer(width = 10.dp)
            DescriptionSmallText(
                text = stat,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = verticalPadding)
            )
            HorizontalSpacer(width = 10.dp)
            DefaultPainterImage(
                resId = com.jinproject.design_ui.R.drawable.ic_arrow_right_small,
                contentDescription = "Right Arrow",
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewHomeScreen(
    @PreviewParameter(HomePreviewParameter::class)
    homeUiState: HomeUiState
) = MiscellaneousToolTheme {
    HomeScreen(
        uiState = homeUiState,
        navigateToDropList = {},
        navigateToCollection = {},
        navigateToAlarm = {},
    )
}

