package com.jinproject.twomillustratedbook.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.LocalTonalElevationEnabled
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.Purchase
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.jinproject.design_compose.component.SnackBarHostCustom
import com.jinproject.design_compose.component.paddingvalues.addStatusBarPadding
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.core.AnalyticsEvent
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.base.CommonDialogFragment
import com.jinproject.features.core.base.item.SnackBarMessage
import com.jinproject.features.core.compose.LocalAnalyticsLoggingEvent
import com.jinproject.twomillustratedbook.BuildConfig
import com.jinproject.twomillustratedbook.BuildConfig.ADMOB_REWARD_ID
import com.jinproject.twomillustratedbook.ui.navigation.NavigationDefaults
import com.jinproject.twomillustratedbook.ui.navigation.NavigationGraph
import com.jinproject.twomillustratedbook.ui.navigation.Router
import com.jinproject.twomillustratedbook.ui.navigation.isBarHasToBeShown
import com.jinproject.twomillustratedbook.ui.navigation.navigationSuiteItems
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result.not()) {
            Toast.makeText(
                applicationContext,
                getString(com.jinproject.design_ui.R.string.authority_alarm_failure),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private var mRewardedAd: RewardedAd? = null

    private var billingCallback: OnBillingCallback? = null

    private fun setBillingCallback(listener: OnBillingCallback) {
        billingCallback = listener
    }

    interface OnBillingCallback {
        fun onSuccess(purchase: Purchase)
        fun onFailure(errorCode: Int)
    }

    lateinit var billingModule: BillingModule

    private val inAppUpdateLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            inAppUpdateManager.inAppUpdatingLauncherResult(result)
        }

    private val inAppUpdateManager by lazy {
        InAppUpdateManager(
            activity = this,
            showDialog = { appUpdateManager, sendMessageIfDenyUpdate ->
                CommonDialogFragment.show(
                    fragmentManager = supportFragmentManager,
                    title = getString(R.string.new_version_message),
                    message = null,
                    positiveButtonText = getString(R.string.new_version_positive_button),
                    negativeButtonText = getString(R.string.new_version_negative_button),
                    listener = object : CommonDialogFragment.Listener() {
                        override fun onPositiveButtonClick(value: String) {
                            appUpdateManager.completeUpdate()
                        }

                        override fun onNegativeButtonClick() {
                            sendMessageIfDenyUpdate()
                        }
                    },
                )
            }
        )
    }

    private val adMobManager by lazy { AdMobManager(this) }

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        initBillingModule()
        firebaseAnalytics = Firebase.analytics

        setContent {
            MiscellaneousToolTheme {
                Content()
            }
        }

        inAppUpdateManager.checkUpdateIsAvailable(launcher = inAppUpdateLauncher)
    }

    private fun initBillingModule() {
        billingModule = BillingModule(
            context = this,
            lifecycleScope = lifecycleScope,
            callback = object : BillingModule.BillingCallback {
                override suspend fun onReady() {
                    billingModule.getPurchasableProducts(listOf(BillingModule.Product.AD_REMOVE))
                        ?.let {
                            it.first()?.let {
                                adMobManager.initAdView()
                            }
                        }
                }

                override fun onSuccess(purchase: Purchase) {
                    billingCallback?.onSuccess(purchase)
                }

                override fun onFailure(errorCode: Int) {
                    billingCallback?.onFailure(errorCode)
                }
            }
        )
    }

    @Composable
    private fun Content(
        coroutineScope: CoroutineScope = rememberCoroutineScope(),
        navController: NavHostController = rememberNavController(),
    ) {
        val snackBarHostState = remember { SnackbarHostState() }

        val isAdViewRemoved by adMobManager.isAdviewRemoved.collectAsStateWithLifecycle()

        val showSnackBar = { snackBarMessage: SnackBarMessage ->
            coroutineScope.launch {
                snackBarHostState.showSnackbar(
                    message = snackBarMessage.headerMessage,
                    actionLabel = snackBarMessage.contentMessage,
                    duration = SnackbarDuration.Indefinite
                )
            }
        }

        DisposableEffect(key1 = Unit) {
            setBillingCallback(
                object : OnBillingCallback {
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
            )
            onDispose { }
        }

        val navBarItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = NavigationDefaults.navigationIndicatorColor()
        )
        val railBarItemColors = NavigationRailItemDefaults.colors(
            indicatorColor = NavigationDefaults.navigationIndicatorColor()
        )
        val drawerItemColors = NavigationDrawerItemDefaults.colors()

        val navigationSuiteItemColors = remember {
            NavigationSuiteItemColors(
                navigationBarItemColors = navBarItemColors,
                navigationRailItemColors = railBarItemColors,
                navigationDrawerItemColors = drawerItemColors,
            )
        }

        val router = remember(navController) { Router(navController) }
        val currentDestination by rememberUpdatedState(newValue = router.currentDestination)
        val currentWindowAdaptiveInfo =
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())

        val layoutType by rememberUpdatedState(
            newValue = if (!currentDestination.isBarHasToBeShown())
                NavigationSuiteType.None
            else
                currentWindowAdaptiveInfo
        )

        CompositionLocalProvider(
            LocalTonalElevationEnabled provides false,
            LocalAnalyticsLoggingEvent provides ::loggingAnalyticsEvent
        ) {
            NavigationSuiteScaffold(
                navigationSuiteItems = {
                    navigationSuiteItems(
                        currentDestination = currentDestination,
                        itemColors = navigationSuiteItemColors,
                        onClick = { topLevelRoute ->
                            router.navigateTopLevelDestination(topLevelRoute)
                        }
                    )
                },
                layoutType = layoutType,
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                navigationSuiteColors = NavigationSuiteDefaults.colors(
                    navigationBarContainerColor = NavigationDefaults.containerColor(),
                    navigationBarContentColor = NavigationDefaults.contentColor(),
                    navigationRailContainerColor = NavigationDefaults.containerColor(),
                    navigationRailContentColor = NavigationDefaults.contentColor(),
                    navigationDrawerContainerColor = NavigationDefaults.containerColor(),
                    navigationDrawerContentColor = NavigationDefaults.contentColor(),
                ),
            ) {
                Column(
                    modifier = Modifier.addStatusBarPadding()
                ) {
                    if (!isAdViewRemoved)
                        AndroidView(
                            modifier = Modifier.fillMaxWidth(),
                            factory = { context ->
                                AdView(context).apply {
                                    setAdSize(AdSize.BANNER)
                                    adUnitId = BuildConfig.ADMOB_UNIT_ID
                                    loadAd(AdRequest.Builder().build())
                                }
                            },
                        )

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        backgroundColor = MaterialTheme.colorScheme.background,
                        snackbarHost = {
                            SnackBarHostCustom(headerMessage = snackBarHostState.currentSnackbarData?.message
                                ?: "",
                                contentMessage = snackBarHostState.currentSnackbarData?.actionLabel
                                    ?: "",
                                snackBarHostState = snackBarHostState,
                                disMissSnackBar = { snackBarHostState.currentSnackbarData?.dismiss() })
                        }
                    ) { paddingValues ->

                        NavigationGraph(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            router = router,
                            billingModule = billingModule,
                            showRewardedAd = { onResult ->
                                showRewardedAd(onResult)
                            },
                            showSnackBar = { snackBarMessage ->
                                showSnackBar(snackBarMessage)
                            },
                        )
                    }
                }
            }
        }
    }

    private fun loggingAnalyticsEvent(event: AnalyticsEvent) {
        if (!BuildConfig.IS_DEBUG_MODE)
            firebaseAnalytics.logEvent(event.eventName) {
                event.logEvent(this)
            }
    }

    private fun loadRewardedAd() {
        RewardedAd.load(
            this,
            ADMOB_REWARD_ID,
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
        if (!BuildConfig.IS_DEBUG_MODE) {
            mRewardedAd?.show(this) {
                onResult()
            } ?: run {
                loadRewardedAd()
                onResult()
            }
        }
        else
            onResult()
    }

    override fun onResume() {
        super.onResume()
        if (billingModule.isReady) {
            lifecycleScope.launch {
                billingModule.queryPurchaseAsync()?.let { purchasedList ->
                    billingModule.approvePurchased(purchasedList)

                    if (billingModule.checkPurchased(
                            purchaseList = purchasedList,
                            productId = BillingModule.Product.AD_REMOVE.id
                        )
                    )
                        adMobManager.updateIsAdViewRemoved(true)
                }
            }
        }
        inAppUpdateManager.checkUpdateIsDownloaded()
        requestPermission()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}