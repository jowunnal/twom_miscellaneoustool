package com.jinproject.design_compose.component.layout

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.paddingvalues.MiscellanousToolPaddingValues
import com.jinproject.design_compose.component.pushRefresh.MTProgressIndicatorRotating

@Stable
sealed interface DownloadableUiState {
    data object Loading : DownloadableUiState

    @Stable
    data class Exception(val t: Throwable) : DownloadableUiState
}

@Stable
abstract class DownLoadedUiState<T> : DownloadableUiState {
    abstract val data: T
}

@Stable
object DefaultDownloadedUiState : DownLoadedUiState<Any>() {
    override val data: Any get() = Any()
}

@Composable
inline fun DownloadableLayout(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    contentPaddingValues: MiscellanousToolPaddingValues = MiscellanousToolPaddingValues(),
    downloadableUiState: DownloadableUiState,
    verticalScrollable: Boolean = false,
    crossinline content: @Composable ColumnScope.(DownLoadedUiState<*>) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        topBar()
        VerticalSpacer(height = 8.dp)

        Crossfade(
            targetState = downloadableUiState == DownloadableUiState.Loading,
            label = "Crossfade Default Layout",
            modifier = Modifier
                .padding(contentPaddingValues)
                .weight(1f)
                .imePadding(),
        ) { isLoading ->
            if (isLoading)
                MTProgressIndicatorRotating(modifier = Modifier)
            else
                if (downloadableUiState is DownloadableUiState.Exception) {
                    ExceptionScreen(
                        headlineMessage = downloadableUiState.t.message.toString(),
                        causeMessage = downloadableUiState.t.cause?.message.toString(),
                    )
                } else if (downloadableUiState is DownLoadedUiState<*>) {
                    Column(
                        modifier = Modifier.then(
                            if (verticalScrollable)
                                Modifier.verticalScroll(rememberScrollState())
                            else
                                Modifier
                        )
                    ) {
                        content(downloadableUiState)
                    }
                }
        }
    }
}

abstract class DownloadableUiStatePreviewParameter<T> :
    PreviewParameterProvider<DownloadableUiState> {
    abstract val data: DownLoadedUiState<T>

    override val values: Sequence<DownloadableUiState>
        get() = sequenceOf(
            data,
            DownloadableUiState.Loading,
            DownloadableUiState.Exception(
                Throwable(
                    message = "예외가 발생했어요.",
                    cause = Throwable("이러이러한 예외가 발생")
                )
            ),
        )
}