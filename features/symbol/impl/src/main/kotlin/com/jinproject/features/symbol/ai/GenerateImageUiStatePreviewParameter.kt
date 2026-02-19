package com.jinproject.features.symbol.ai

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.persistentListOf
import java.time.ZonedDateTime

internal class GenerateImageUiStatePreviewParameter :
    PreviewParameterProvider<GenerateImageUiState> {
    private val now = ZonedDateTime.now()

    override val values: Sequence<GenerateImageUiState>
        get() = sequenceOf(
            GenerateImageUiState(
                messages = persistentListOf(
                    Message(
                        publisher = Publisher.User,
                        data = "안녕",
                        timeStamp = now.minusSeconds(30).toEpochSecond(),
                    ),
                    Message(
                        publisher = Publisher.AI,
                        data = "안녕하세요.",
                        timeStamp = now.minusSeconds(25).toEpochSecond(),
                    ),
                    Message(
                        publisher = Publisher.User,
                        data = "반가워",
                        timeStamp = now.minusSeconds(20).toEpochSecond(),
                    ),
                    Message(
                        publisher = Publisher.AI,
                        data = "저도 반가워요.",
                        timeStamp = now.minusSeconds(15).toEpochSecond(),
                    ),
                    Message(
                        publisher = Publisher.User,
                        data = "반가워",
                        timeStamp = now.minusSeconds(14).toEpochSecond(),
                    ),
                    Message(
                        publisher = Publisher.AI,
                        data = "저도 반가워요.",
                        timeStamp = now.minusSeconds(13).toEpochSecond(),
                    ),
                    Message(
                        publisher = Publisher.User,
                        data = "반가워",
                        timeStamp = now.minusSeconds(12).toEpochSecond(),
                    ),
                    Message(
                        publisher = Publisher.AI,
                        data = "http저도 반가워요.",
                        timeStamp = now.minusSeconds(11).toEpochSecond(),
                    ),
                    Message(
                        publisher = Publisher.User,
                        data = "반가워",
                        timeStamp = now.minusSeconds(10).toEpochSecond(),
                    ),
                    Message(
                        publisher = Publisher.AI,
                        data = "http저도 반가워요.",
                        timeStamp = now.minusSeconds(9).toEpochSecond(),
                    ),
                    Message(
                        publisher = Publisher.User,
                        data = "반가워",
                        timeStamp = now.minusSeconds(8).toEpochSecond(),
                    ),
                    Message(
                        publisher = Publisher.AI,
                        data = "http저도 반가워요.",
                        timeStamp = now.minusSeconds(7).toEpochSecond(),
                    ),
                )
            )
        )
}