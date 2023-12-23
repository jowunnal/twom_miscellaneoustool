package com.jinproject.design_compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Immutable
@JvmInline
value class MiscellaneousToolColor private constructor(val color: Color){
    companion object {
        @Stable
        private val primary = MiscellaneousToolColor(Color(0xFF7FD182))
        @Stable
        private val deep_primary = MiscellaneousToolColor(Color(0xFF315532))
        @Stable
        private val lightBlack = MiscellaneousToolColor(Color(0xFF1B1B1E))
        @Stable
        private val black = MiscellaneousToolColor(Color(0xFF151515))
        @Stable
        private val white = MiscellaneousToolColor(Color(0XFFFFFFFF))
        @Stable
        private val grey_100 = MiscellaneousToolColor(Color(0xFFF5F5F5))
        @Stable
        private val grey_200 = MiscellaneousToolColor(Color(0xFFEEEEEE))
        @Stable
        private val grey_300 = MiscellaneousToolColor(Color(0xFFE0E0E0))
        @Stable
        private val grey_400 = MiscellaneousToolColor(Color(0xFFBDBDBD))
        @Stable
        private val grey_500 = MiscellaneousToolColor(Color(0xFF9E9E9E))
        @Stable
        private val grey_600 = MiscellaneousToolColor(Color(0xFF757575))
        @Stable
        private val grey_700 = MiscellaneousToolColor(Color(0xFF616161))
        @Stable
        private val grey_800 = MiscellaneousToolColor(Color(0xFF424242))
        @Stable
        private val grey_900 = MiscellaneousToolColor(Color(0xFF212121))
        @Stable
        val red = MiscellaneousToolColor(Color(0xFFE0302D))
        @Stable
        private val deepRed = MiscellaneousToolColor(Color(0xFF800006))
        @Stable
        val blue = MiscellaneousToolColor(Color(0xFF007AFF))

        val light_primary = primary
        val light_onPrimary = white
        @Stable
        val light_secondary = MiscellaneousToolColor(Color(0xFF91E4E1))
        val light_onSecondary = grey_600
        val light_error = red
        @Stable
        val light_onError = MiscellaneousToolColor(Color(0xFF410001))
        val light_background = white
        val light_onBackground = lightBlack
        val light_surface = white
        val light_onSurface = lightBlack
        val light_onSurfaceVariant = grey_400
        val light_scrim = grey_600
        val light_outline = grey_600

        val dark_primary = deep_primary
        val dark_onPrimary = grey_300
        @Stable
        val dark_secondary = MiscellaneousToolColor(Color(0xFFD599E3))
        val dark_onSecondary = grey_300
        @Stable
        val dark_error = MiscellaneousToolColor(Color(0xFFFFB4A9))
        val dark_onError = deepRed
        val dark_background = black // 컨테이너 색상
        val dark_onBackground = grey_600
        val dark_surface = lightBlack // 상단바 색상
        val dark_onSurface = grey_600
        val dark_onSurfaceVariant = grey_200
        val dark_scrim = grey_300
        val dark_outline = grey_300
    }
}