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
        private val lightGray = MiscellaneousToolColor(Color(0xFFDADDE1))
        @Stable
        private val mediumGray = MiscellaneousToolColor(Color(0xFF8A8A8A))
        @Stable
        private val deepGray = MiscellaneousToolColor(Color(0xFF575757))
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
        val light_onSecondary = mediumGray
        val light_error = red
        @Stable
        val light_onError = MiscellaneousToolColor(Color(0xFF410001))
        val light_background = white
        val light_onBackground = lightBlack
        val light_surface = white
        val light_onSurface = lightBlack
        val light_scrim = mediumGray
        val light_outline = mediumGray

        val dark_primary = deep_primary
        val dark_onPrimary = lightGray
        @Stable
        val dark_secondary = MiscellaneousToolColor(Color(0xFFD599E3))
        val dark_onSecondary = lightGray
        @Stable
        val dark_error = MiscellaneousToolColor(Color(0xFFFFB4A9))
        val dark_onError = deepRed
        val dark_background = black // 컨테이너 색상
        val dark_onBackground = lightGray
        val dark_surface = lightBlack // 상단바 색상
        val dark_onSurface = lightGray
        val dark_scrim = lightGray
        val dark_outline = lightGray
    }
}