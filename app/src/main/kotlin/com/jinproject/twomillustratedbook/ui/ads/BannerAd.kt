package com.jinproject.twomillustratedbook.ui.ads

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.jinproject.twomillustratedbook.BuildConfig

@Composable
fun BannerAd(
    adsVisibility: Boolean,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
) {
    AnimatedVisibility(adsVisibility) {
        val adView = remember {
            AdView(context).apply {
                adUnitId = BuildConfig.ADMOB_UNIT_ID
                setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, 360))
            }
        }

        if (LocalInspectionMode.current) {
            Text(
                text = "Google Mobile Ads preview banner.",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
            )
            return@AnimatedVisibility
        }

        AndroidView(
            modifier = modifier.wrapContentSize(),
            factory = { adView },
        )

        LifecycleResumeEffect(adView) {
            adView.resume()
            onPauseOrDispose { adView.pause() }
        }

        DisposableEffect(Unit) {
            onDispose {
                adView.destroy()
            }
        }
    }
}