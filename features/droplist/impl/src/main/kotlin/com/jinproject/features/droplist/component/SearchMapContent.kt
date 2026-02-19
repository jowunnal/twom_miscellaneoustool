package com.jinproject.features.droplist.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.jinproject.design_compose.component.CoilBasicImage
import com.jinproject.design_compose.component.HorizontalDivider
import com.jinproject.design_compose.component.HorizontalWeightSpacer
import com.jinproject.design_compose.component.SubcomposeAsyncImageWithPreview
import com.jinproject.design_compose.component.button.clickableAvoidingDuplication
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating
import com.jinproject.design_compose.component.text.SearchTextField
import com.jinproject.design_compose.component.text.TitleSmallText
import com.jinproject.design_compose.theme.MiscellaneousToolColor
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.domain.entity.MonsterType
import com.jinproject.features.core.utils.toAssetImageUri
import com.jinproject.features.droplist.DropListUiState
import com.jinproject.features.droplist.DropListUiStatePreviewParameter
import com.jinproject.features.droplist.state.MonsterState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter

@OptIn(FlowPreview::class)
@Composable
fun SearchMapContent(
    dropListUiState: DropListUiState,
    transitionState: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    softwareKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    setTransitionState: (Boolean) -> Unit,
) {
    val textFieldState = rememberTextFieldState(dropListUiState.searchQuery)

    LaunchedEffect(dropListUiState) {
        snapshotFlow { textFieldState.text.toString() }
            .debounce(300)
            .collectLatest { text ->
                dropListUiState.updateSearchQuery(text)
            }
    }

    val backgroundColor = MaterialTheme.colorScheme.background
    val cursorColor = MaterialTheme.colorScheme.onBackground

    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp)
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
                changeExitIconVisibility = { visible ->
                    if (!visible) {
                        textFieldState.edit { replace(0, length, "") }
                        dropListUiState.updateSearchQuery("")
                    }
                    setTransitionState(visible)
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
                        items(dropListUiState.searchables, key = { searchable -> searchable.name }) { searchable ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem()
                                    .clickableAvoidingDuplication {
                                        dropListUiState.setSelectedSearchable(searchable)
                                        softwareKeyboardController?.let {
                                            softwareKeyboardController.hide()
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                CoilBasicImage(
                                    data = toAssetImageUri(
                                        prefix = searchable.imagePrefix,
                                        imgName = searchable.imageName
                                    ),
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .size(24.dp),
                                    error = {
                                        Image(
                                            imageVector = ImageVector.vectorResource(R.drawable.ic_help),
                                            contentDescription = "Not exists image",
                                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceVariant)
                                        )
                                    },
                                )
                                TitleSmallText(
                                    text = searchable.name,
                                    color = when (searchable) {
                                        is MonsterState -> when (searchable.type) {
                                            is MonsterType.Normal -> MiscellaneousToolColor.lightBlack.color
                                            is MonsterType.Named -> MiscellaneousToolColor.blue.color
                                            is MonsterType.Boss -> MiscellaneousToolColor.orange.color
                                            is MonsterType.WorldBoss -> MiscellaneousToolColor.deepRed.color
                                        }
                                        else -> MiscellaneousToolColor.lightBlack.color
                                    },
                                )
                                HorizontalWeightSpacer(1f)
                                Image(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right_small),
                                    contentDescription = "몬스터 선택",
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

@Preview(showBackground = true)
@Composable
private fun PreviewSearchMapContent(
    @PreviewParameter(DropListUiStatePreviewParameter::class)
    dropListUiState: DropListUiState
) = MiscellaneousToolTheme {
    SharedTransitionLayout {
        SearchMapContent(
            dropListUiState = dropListUiState,
            transitionState = true,
            sharedTransitionScope = this@SharedTransitionLayout,
            setTransitionState = {},
        )
    }
}
