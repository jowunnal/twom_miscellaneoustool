package com.jinproject.design_compose.component.paddingvalues

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Immutable
data class MiscellanousToolPaddingValues(
    @Stable
    private val start: Dp = 0.dp,
    @Stable
    private val top: Dp = 0.dp,
    @Stable
    private val end: Dp = 0.dp,
    @Stable
    private val bottom: Dp = 0.dp
): PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection) = start

    override fun calculateTopPadding() = top

    override fun calculateRightPadding(layoutDirection: LayoutDirection) = end

    override fun calculateBottomPadding() = bottom

    operator fun plus(other: PaddingValues): PaddingValues = PaddingValues(
        top = top + other.calculateTopPadding(),
        start = start + other.calculateStartPadding(LayoutDirection.Ltr),
        end = end + other.calculateEndPadding(LayoutDirection.Ltr),
        bottom = bottom + other.calculateBottomPadding(),
    )

    companion object {
        fun fromPaddingValues(paddingValues: PaddingValues): MiscellanousToolPaddingValues = MiscellanousToolPaddingValues(
            top = paddingValues.calculateTopPadding(),
            start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
            end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
            bottom = paddingValues.calculateBottomPadding(),
        )
    }
}