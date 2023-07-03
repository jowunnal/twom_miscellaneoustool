package com.jinproject.twomillustratedbook.ui.screen.compose.navigation

import android.app.AlarmManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.Purchase
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.MainActivity
import com.jinproject.twomillustratedbook.ui.base.item.SnackBarMessage
import com.jinproject.twomillustratedbook.ui.screen.compose.theme.MiscellaneousToolTheme
import com.jinproject.twomillustratedbook.ui.screen.gear.GearViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NavigationFragment : Fragment(), MainActivity.OnBillingCallback {
    private var mRewardedAd: RewardedAd? = null
    private val gearViewModel: GearViewModel by viewModels()

    override fun onSuccess(purchase: Purchase) {
        gearViewModel.emitSnackBar(SnackBarMessage(
            headerMessage = "${purchase.products.first()} 상품의 구매가 완료되었어요."
        ))
    }

    override fun onFailure(errorCode: Int) {
        Log.e("test","error : $errorCode")
        gearViewModel.emitSnackBar(SnackBarMessage(
            headerMessage = "구매 실패",
            contentMessage = when(errorCode) {
                1 -> "취소를 하셨어요."
                2,3,4 -> "유효하지 않은 상품 이에요."
                5,6 -> "잘못된 상품 이에요."
                7 -> "이미 보유하고 있는 상품 이에요."
                else -> "네트워크 에러로 인해 실패했어요."
            }
        ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadRewardedAd()
    }

    override fun onResume() {
        if(Build.VERSION.SDK_INT >= 31) {
            val alarmManager = requireActivity().getSystemService(AlarmManager::class.java)
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(requireContext(),"권한을 거부하시면 정확한 알람을 받으실 수 없습니다.",Toast.LENGTH_LONG).show()
            }
        }
        super.onResume()
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

    @Composable
    private fun Content() {
        hideTopBar()
        val activity = (activity as MainActivity).apply {
            setBillingCallback(this@NavigationFragment)
        }
        val billingModule = activity.billingModule
        NavigationGraph(
            navController = rememberNavController(),
            changeVisibilityBottomNavigationBar = { bool -> changeVisibilityBottomNavigationBar(bool) },
            showRewardedAd = {
                showRewardedAd(
                    mRewardedAd = mRewardedAd,
                    billingModule = billingModule
                )
            },
            billingModule = billingModule,
            gearViewModel = gearViewModel
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

    private fun showRewardedAd(mRewardedAd: RewardedAd?, billingModule: BillingModule) {
        viewLifecycleOwner.lifecycleScope.launch {
            billingModule.queryPurchase { purchaseList ->
                if(billingModule.checkPurchased(purchaseList = purchaseList, productId = "ad_remove")) {
                    mRewardedAd?.let { ad ->
                        ad.show(requireActivity(), OnUserEarnedRewardListener { rewardItem ->
                            val rewardAmount = rewardItem.amount
                            val rewardType = rewardItem.type
                        })
                    } ?: run {}
                }
            }
        }
    }

    override fun onDestroy() {
        mRewardedAd = null
        super.onDestroy()
    }
}