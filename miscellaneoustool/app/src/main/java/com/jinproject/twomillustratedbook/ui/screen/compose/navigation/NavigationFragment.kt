package com.jinproject.twomillustratedbook.ui.screen.compose.navigation

import android.app.AlarmManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.MiscellaneousToolTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationFragment : Fragment() {
    private var mRewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadRewardedAd()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MiscellaneousToolTheme {
                    this@NavigationFragment.Content()
                }
            }
        }
    }

    override fun onResume() {
        if(Build.VERSION.SDK_INT >= 31) {
            val alarmManager = requireActivity().getSystemService(AlarmManager::class.java)
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(requireContext(),"권한을 거부하시면 정확한 알람을 받으실 수 없습니다.", Toast.LENGTH_LONG).show()
            }
        }
        super.onResume()
    }

    @Composable
    private fun Content() {
        hideTopBar()
        NavigationGraph(
            navController = rememberNavController(),
            changeVisibilityBottomNavigationBar = { bool -> changeVisibilityBottomNavigationBar(bool) },
            showRewardedAd = { onResult ->
                showRewardedAd(onResult = onResult)
            }
        )
    }

    private fun changeVisibilityBottomNavigationBar(bottomNavigationBarVisibility: Boolean) {
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .visibility = if (bottomNavigationBarVisibility) View.VISIBLE else View.GONE
    }

    private fun hideTopBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    private fun loadRewardedAd() {
        RewardedAd.load(
            requireActivity(),
            requireActivity().getString(R.string.rewarded_Ad_UnitId),
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
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
            })
    }

    private fun showRewardedAd(onResult:() -> Unit) {
        mRewardedAd?.let { ad ->
            ad.show(requireActivity()) {
                onResult()
            }
        } ?: run {
            loadRewardedAd()
            onResult()
        }
    }

    override fun onDestroy() {
        mRewardedAd = null
        super.onDestroy()
    }
}