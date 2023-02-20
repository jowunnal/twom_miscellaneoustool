package com.jinproject.twomillustratedbook.ui.screen.compose.navigation

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            changeVisibilityBottomNavigationBar = { bool -> changeVisibilityBottomNavigationBar(bool) }
        )
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

    private fun showRewaredAd(mRewardedAd: RewardedAd?) {
        mRewardedAd?.show(requireActivity(), OnUserEarnedRewardListener() {
            fun onUserEarnedReward(rewardItem: RewardItem) {
                var rewardAmount = rewardItem.amount
                var rewardType = rewardItem.type
            }
        })
    }

    private fun checkAuthorityDrawOverlays(): Boolean { // 다른앱 위에 그리기 체크 : true = 권한있음 , false = 권한없음
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (!Settings.canDrawOverlays(requireActivity())) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + requireActivity().packageName)
                )
                startActivityForResult(intent, 404)
                return false
            } else {
                return true
            }
        } else {
            Snackbar.make(requireView(), "다른앱 위에 그리기 권한 설정을 해주셔야 합니다.", Snackbar.LENGTH_SHORT)
                .show()
            return false
        }
    }
}