package com.jinproject.twomillustratedbook.ui.ads

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.annotation.concurrent.Immutable

@Immutable
class AdMobManager {
    private var _isAdViewRemoved = MutableStateFlow(true)
    val isAdviewRemoved = _isAdViewRemoved.asStateFlow()

    fun initAdView() {
        updateIsAdViewRemoved(false)
    }

    fun updateIsAdViewRemoved(boolean: Boolean) = _isAdViewRemoved.update { boolean }
}