package com.jinproject.design_compose.component.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.remember

@Composable
@NonRestartableComposable
fun RememberEffect(
    key1: Any?,
    block: () -> Unit
) {
    remember(key1) { RememberEffectImpl(block) }
}

internal class RememberEffectImpl(
    private val task: () -> Unit
) : RememberObserver {
    override fun onRemembered() {
        task.invoke()
    }

    override fun onForgotten() {}

    override fun onAbandoned() {}
}