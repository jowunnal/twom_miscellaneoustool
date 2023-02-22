package com.jinproject.twomillustratedbook.ui.screen.compose.navigation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.TwomIllustratedBookTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationFragment : Fragment() {
    private var mRewardedAd: RewardedAd? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TwomIllustratedBookTheme {
                    this@NavigationFragment.Content()
                }
            }
        }
    }

    @Composable
    private fun Content() {
        NavigationGraph(
            navController = rememberNavController(),
            changeVisibilityBottomNavigationBar = { bool -> changeVisibilityBottomNavigationBar(bool) },
            showRewardedAd = { showRewardedAd(mRewardedAd) },
            checkAuthorityDrawOverlays = { context:Context, register:(Intent) -> Unit
                -> checkAuthorityDrawOverlays(context, register)
            }
        )
        loadRewardedAd()
    }

    private fun changeVisibilityBottomNavigationBar(bottomNavigationBarVisibility: Boolean) {
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .visibility = if (bottomNavigationBarVisibility) View.VISIBLE else View.GONE
    }

    private fun loadRewardedAd() {
        RewardedAd.load(
            requireActivity(),
            requireActivity().getString(R.string.Rewarded_Ad_UnitId),
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                }
            })
        mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                mRewardedAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                mRewardedAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
            }
        }
    }

    private fun checkAuthorityDrawOverlays(
        context: Context,
        registerForActivityResult: (Intent) -> Unit
    ): Boolean { // 다른앱 위에 그리기 체크 : true = 권한있음 , false = 권한없음
        return if (!Settings.canDrawOverlays(context)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.packageName)
            )
            registerForActivityResult(intent)
            false
        } else {
            true
        }
    }


    private fun showRewardedAd(mRewardedAd: RewardedAd?) {
        mRewardedAd?.show(requireActivity(), OnUserEarnedRewardListener() {
            fun onUserEarnedReward(rewardItem: RewardItem) {
                var rewardAmount = rewardItem.amount
                var rewardType = rewardItem.type
            }
        })
    }

    override fun onDestroy() {
        mRewardedAd = null
        super.onDestroy()
    }
}