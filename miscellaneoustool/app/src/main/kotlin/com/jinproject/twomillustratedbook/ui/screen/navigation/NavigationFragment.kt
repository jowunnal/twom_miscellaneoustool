package com.jinproject.twomillustratedbook.ui.screen.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.navigation.fragment.findNavController
import com.android.billingclient.api.Purchase
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.jinproject.design_compose.component.SnackBarHostCustom
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.features.alarm.AlarmRoute
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.utils.findActivity
import com.jinproject.features.simulator.SimulatorRoute
import com.jinproject.features.symbol.SymbolRoute
import com.jinproject.twomillustratedbook.BuildConfig.ADMOB_REAL_REWARD_ID
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
        savedInstanceState: Bundle?,
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRewardedAd()
    }

    @Composable
    private fun Content(
        coroutineScope: CoroutineScope = rememberCoroutineScope(),
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

        val callback = remember {
            object : MainActivity.OnBillingCallback {
                override fun onSuccess(purchase: Purchase) {
                    showSnackBar(
                        SnackBarMessage(
                            headerMessage = "${purchase.products.first()} 상품의 구매가 완료되었어요."
                        )
                    )
                }

                override fun onFailure(errorCode: Int) {
                    showSnackBar(
                        SnackBarMessage(
                            headerMessage = "구매 실패",
                            contentMessage = when (errorCode) {
                                1 -> "취소를 하셨어요."
                                2, 3, 4 -> "유효하지 않은 상품 이에요."
                                5, 6 -> "잘못된 상품 이에요."
                                7 -> "이미 보유하고 있는 상품 이에요."
                                else -> "네트워크 에러로 인해 실패했어요."
                            }
                        )
                    )
                }
            }
        }

        val activity = (requireActivity().findActivity() as MainActivity).apply {
            setBillingCallback(callback)
        }
        val billingModule = remember(activity) { activity.billingModule }
        val startDestination =
            when(findNavController().currentBackStackEntry?.arguments?.getString("start")) {
                "alarm" -> AlarmRoute.AlarmGraph
                "symbolGraph" -> SymbolRoute.SymbolGraph
                "simulator" -> SimulatorRoute.SimulatorGraph
                else -> AlarmRoute.AlarmGraph
            }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = MaterialTheme.colorScheme.background,
                snackbarHost = {
                    SnackBarHostCustom(headerMessage = snackBarHostState.currentSnackbarData?.message
                        ?: "",
                        contentMessage = snackBarHostState.currentSnackbarData?.actionLabel ?: "",
                        snackBarHostState = snackBarHostState,
                        disMissSnackBar = { snackBarHostState.currentSnackbarData?.dismiss() })
                }
            ) { paddingValues ->
                NavigationGraph(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    navHostController = rememberNavController(),
                    startDestination = startDestination,
                    billingModule = billingModule,
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

    private fun hideTopBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    private fun loadRewardedAd() {
        RewardedAd.load(
            requireActivity(),
            ADMOB_REAL_REWARD_ID,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                    mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdClicked() {}

                        override fun onAdDismissedFullScreenContent() {
                            mRewardedAd = null
                            loadRewardedAd()
                        }

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            mRewardedAd = null
                        }

                        override fun onAdImpression() {}

                        override fun onAdShowedFullScreenContent() {}
                    }
                }
            })
    }

    private fun showRewardedAd(onResult: () -> Unit) {
        mRewardedAd?.show(requireActivity()) {
            onResult()
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