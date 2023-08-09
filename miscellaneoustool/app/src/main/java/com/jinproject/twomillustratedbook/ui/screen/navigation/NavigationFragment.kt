package com.jinproject.twomillustratedbook.ui.screen.navigation

import android.app.AlarmManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.Purchase
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jinproject.design_compose.component.SnackBarHostCustom
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.utils.findActivity
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NavigationFragment : Fragment() {
    private var mRewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideTopBar()
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
        super.onResume()
        if(Build.VERSION.SDK_INT >= 31) {
            val alarmManager = requireActivity().getSystemService(AlarmManager::class.java)
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(requireContext(),"권한을 거부하시면 정확한 알람을 받으실 수 없습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    @Composable
    private fun Content(
        coroutineScope: CoroutineScope = rememberCoroutineScope()
    ) {
        val snackBarHostState = remember { SnackbarHostState() }

        val showSnackBar = { snackBarMessage: SnackBarMessage ->
            coroutineScope.launch {
                snackBarHostState.showSnackbar(
                    message = snackBarMessage.headerMessage,
                    actionLabel = snackBarMessage.contentMessage,
                    duration = SnackbarDuration.Indefinite
                )
            }
        }

        val callback = object: MainActivity.OnBillingCallback {
            override fun onSuccess(purchase: Purchase) {
                showSnackBar(
                    SnackBarMessage(
                        headerMessage = "${purchase.products.first()} 상품의 구매가 완료되었어요."
                    )
                )
            }

            override fun onFailure(errorCode: Int) {
                Log.e("test","error : $errorCode")
                val snackBar = SnackBarMessage(
                    headerMessage = "구매 실패",
                    contentMessage = when (errorCode) {
                        1 -> "취소를 하셨어요."
                        2, 3, 4 -> "유효하지 않은 상품 이에요."
                        5, 6 -> "잘못된 상품 이에요."
                        7 -> "이미 보유하고 있는 상품 이에요."
                        else -> "네트워크 에러로 인해 실패했어요."
                    }
                )
                showSnackBar(snackBar)
            }

        }

        val activity = (requireActivity().findActivity() as MainActivity).apply {
            setBillingCallback(callback)
        }
        val billingModule = activity.billingModule

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = MaterialTheme.colorScheme.background,
                snackbarHost = {
                    SnackBarHostCustom(headerMessage = snackBarHostState.currentSnackbarData?.message ?: "",
                        contentMessage = snackBarHostState.currentSnackbarData?.actionLabel ?: "",
                        snackBarHostState = snackBarHostState,
                        disMissSnackBar = { snackBarHostState.currentSnackbarData?.dismiss() })
                }
            ) { paddingValues ->
                NavigationGraph(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    navController = rememberNavController(),
                    billingModule = billingModule,
                    changeVisibilityBottomNavigationBar = { bool -> changeVisibilityBottomNavigationBar(bool) },
                    showRewardedAd = { onResult ->
                        showRewardedAd(onResult)
                    },
                    showSnackBar = { snackBarMessage ->
                        showSnackBar(snackBarMessage)
                    }
                )
            }
        }
    }

    private fun changeVisibilityBottomNavigationBar(bottomNavigationBarVisibility: Boolean) {
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .visibility = if (bottomNavigationBarVisibility) View.VISIBLE else View.GONE
    }

    private fun hideTopBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    private fun loadRewardedAd(onResult:() -> Unit) {
        RewardedAd.load(
            requireActivity(),
            requireActivity().getString(R.string.reward_test_id),
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("test","rewardAd Failed")
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.d("test","rewardAd loaded")
                    mRewardedAd = rewardedAd
                    mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdClicked() {}

                        override fun onAdDismissedFullScreenContent() {
                            mRewardedAd = null
                        }

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            mRewardedAd = null
                        }

                        override fun onAdImpression() {}

                        override fun onAdShowedFullScreenContent() {}
                    }

                    mRewardedAd?.show(requireActivity()) {
                        onResult()
                    }
                }
            })
    }

    private fun showRewardedAd(onResult:() -> Unit) {
        loadRewardedAd(onResult = onResult)
    }

    override fun onDestroy() {
        mRewardedAd = null
        super.onDestroy()
    }
}