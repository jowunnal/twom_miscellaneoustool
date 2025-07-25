package com.jinproject.features.alarm.alarm.component

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.jinproject.design_compose.component.DialogState
import com.jinproject.design_compose.component.HorizontalDivider
import com.jinproject.design_compose.component.HorizontalWeightSpacer
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.button.TextCombinedButton
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating
import com.jinproject.design_compose.component.text.SearchTextField
import com.jinproject.design_compose.component.text.TitleSmallText
import com.jinproject.design_compose.theme.MiscellaneousToolColor
import com.jinproject.design_compose.utils.PreviewMiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.alarm.alarm.AlarmUiState
import com.jinproject.features.alarm.alarm.AlarmUiStatePreviewParameter
import com.jinproject.features.alarm.alarm.item.MonsterState
import com.jinproject.features.alarm.alarm.item.MonsterType
import com.jinproject.features.core.utils.toAssetImageUri
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class, FlowPreview::class)
@Composable
fun SearchBossContent(
    transitionState: Boolean,
    setTransitionState: (Boolean) -> Unit,
    softwareKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    bossNameList: ImmutableList<MonsterState>,
    sharedTransitionScope: SharedTransitionScope,
    addBossToFrequentlyUsedList: (String) -> Unit,
) {

    val textFieldState = rememberTextFieldState()
    var searchedBossList: List<MonsterState> by remember {
        mutableStateOf(bossNameList)
    }

    LaunchedEffect(bossNameList) {
        snapshotFlow { textFieldState.text }
            .distinctUntilChanged()
            .debounce(200)
            .filter { it.isNotBlank() }
            .collectLatest { text ->
                searchedBossList = bossNameList.filter { it.name.contains(text) }
            }
    }

    val backgroundColor = MaterialTheme.colorScheme.background
    val cursorColor = MaterialTheme.colorScheme.onBackground
    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .padding(bottom = 8.dp)
                .onFocusChanged {
                    if (it.hasFocus) {
                        setTransitionState(true)
                    }
                }
                .graphicsLayer {
                    clip = true
                    shape = RoundedCornerShape(20)
                    shadowElevation = 8.dp.value
                }
                .drawBehind {
                    drawRoundRect(color = backgroundColor, cornerRadius = CornerRadius(20f))
                }
        ) {
            SearchTextField(
                modifier = Modifier,
                textFieldState = textFieldState,
                borderColor = backgroundColor,
                backgroundColor = backgroundColor,
                cursorBrush = SolidColor(cursorColor),
                exitIconVisibility = transitionState,
                changeExitIconVisibility = { bool ->
                    if (!bool) {
                        searchedBossList = emptyList()
                    }
                    setTransitionState(bool)
                }
            )
            AnimatedVisibility(transitionState) {
                Column(
                    modifier = Modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(textFieldState.text),
                        animatedVisibilityScope = this
                    )
                ) {
                    HorizontalDivider()
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(MaterialTheme.typography.titleSmall.lineHeight.value.dp * 3 + 24.dp + 12.dp + 12.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
                    ) {
                        items(searchedBossList, key = { monster -> monster.name }) { monster ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickableAvoidingDuplication {
                                        addBossToFrequentlyUsedList(monster.name)
                                        softwareKeyboardController?.let {
                                            softwareKeyboardController.hide()
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                SubcomposeAsyncImageWithPreview(
                                    model = ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(
                                            toAssetImageUri(
                                                prefix = "monster",
                                                imgName = monster.imageName
                                            )
                                        )
                                        .build(),
                                    contentDescription = "Image",
                                    loading = {
                                        MTProgressIndicatorRotating()
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .size(24.dp),
                                    contentScale = ContentScale.Fit,
                                    placeHolderPreview = R.drawable.test,
                                )
                                TitleSmallText(
                                    text = monster.name,
                                    color = when (monster.type) {
                                        MonsterType.Normal -> MaterialTheme.colorScheme.onPrimary
                                        MonsterType.Named -> MiscellaneousToolColor.blue.color
                                        MonsterType.Boss -> MiscellaneousToolColor.orange.color
                                        MonsterType.WorldBoss -> MiscellaneousToolColor.deepRed.color
                                    },
                                )
                                HorizontalWeightSpacer(1f)
                                Image(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right_small),
                                    contentDescription = "오른쪽 클릭",
                                    modifier = Modifier.padding(end = 4.dp),
                                    colorFilter = ColorFilter.tint(
                                        MaterialTheme.colorScheme.outline
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun BossSelectionItem(
    bossName: String,
    context: Context = LocalContext.current,
    onClickBossItem: (String) -> Unit,
    removeBossFromFrequentlyUsedList: (String) -> Unit,
    onOpenDialog: (DialogState) -> Unit,
    onCloseDialog: () -> Unit
) {
    TextCombinedButton(
        text = bossName,
        modifier = Modifier
            .padding(vertical = 8.dp),
        onClick = { onClickBossItem(bossName) },
        onLongClick = {
            onOpenDialog(
                DialogState(
                    header = "$bossName ${context.getString(R.string.delete_something)}",
                    positiveMessage = context.getString(R.string.yes),
                    negativeMessage = context.getString(R.string.no),
                    onPositiveCallback = {
                        removeBossFromFrequentlyUsedList(bossName)
                        onCloseDialog()
                    },
                    onNegativeCallback = { onCloseDialog() }
                )
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewBossSelectionItem() =
    PreviewMiscellaneousToolTheme {
        BossSelectionItem(
            bossName = "보스이름",
            onClickBossItem = {},
            removeBossFromFrequentlyUsedList = {},
            onOpenDialog = {},
            onCloseDialog = {}
        )
    }


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun PreviewBossSelection(
    @PreviewParameter(AlarmUiStatePreviewParameter::class)
    alarmUiState: AlarmUiState,
) =
    PreviewMiscellaneousToolTheme {
        SharedTransitionLayout {
            SearchBossContent(
                bossNameList = alarmUiState.monsterList,
                transitionState = true,
                sharedTransitionScope = this@SharedTransitionLayout,
                addBossToFrequentlyUsedList = {},
                setTransitionState = {},
            )
        }
    }