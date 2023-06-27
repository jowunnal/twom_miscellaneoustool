package com.miscellaneoustool.app.ui.screen.watch.item

import androidx.compose.runtime.Stable

@Stable
enum class ButtonStatus(val displayName: String) {
    ON(displayName = "ON"),
    OFF(displayName = "OFF")
}
