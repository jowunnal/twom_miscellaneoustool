package com.jinproject.twomillustratedbook.ui

import android.app.Activity
import com.google.android.gms.ads.MobileAds
import com.jinproject.twomillustratedbook.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.annotation.concurrent.Immutable

@Immutable
class AdMobManager(
    private val activity: Activity,
) {
    private var _isAdViewRemoved = MutableStateFlow(true)
    val isAdviewRemoved = _isAdViewRemoved.asStateFlow()

    fun initAdView() {
        MobileAds.initialize(activity) { }
        updateIsAdViewRemoved(false)
    }

    fun updateIsAdViewRemoved(boolean: Boolean) = _isAdViewRemoved.update { boolean }
}