package com.jinproject.twomillustratedbook.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTonalElevationEnabled
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
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
import com.jinproject.features.core.toProduct
import com.jinproject.twomillustratedbook.BuildConfig
import com.jinproject.twomillustratedbook.BuildConfig.ADMOB_REWARD_ID
import com.jinproject.twomillustratedbook.ui.ads.AdMobManager
import com.jinproject.twomillustratedbook.ui.ads.BannerAd
import com.jinproject.twomillustratedbook.ui.navigation.NavigationDefaults
import com.jinproject.twomillustratedbook.ui.navigation.NavigationGraph
import com.jinproject.twomillustratedbook.ui.navigation.isBarHasToBeShown
import com.jinproject.twomillustratedbook.ui.navigation.navigationSuiteItems
import com.jinproject.twomillustratedbook.ui.navigation.rememberRouter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val adMobManager by lazy { AdMobManager() }

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics

        setContent {
            MiscellaneousToolTheme {
                Content()
            }
        }

        inAppUpdateManager.checkUpdateIsAvailable(launcher = inAppUpdateLauncher)
        MobileAds.initialize(this) {
            loadRewardedAd()
        }
    }

    @Composable
    private fun Content(
        navController: NavHostController = rememberNavController(),
        coroutineScope: CoroutineScope = rememberCoroutineScope(),
    ) {
        val snackBarHostState = remember { SnackbarHostState() }

        val snackBarChannel = remember {
            Channel<SnackBarMessage>(Channel.CONFLATED)
        }

        LaunchedEffect(key1 = snackBarChannel) {
            snackBarChannel.receiveAsFlow()
                .distinctUntilChanged { old, new -> old.headerMessage == new.headerMessage && old.contentMessage == new.contentMessage }
                .collectLatest { snackBarMessage ->
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(
                        message = snackBarMessage.headerMessage,
                        actionLabel = snackBarMessage.contentMessage,
                        duration = SnackbarDuration.Indefinite,
                    )
                }
        }

        val isAdViewRemoved by adMobManager.isAdviewRemoved.collectAsStateWithLifecycle()

        val showSnackBar = { snackBarMessage: SnackBarMessage ->
            snackBarChannel.trySend(snackBarMessage)
        }

        val billingModule = remember {
            BillingModule(
                activity = this,
                coroutineScope = coroutineScope,
            )
        }

        DisposableEffect(key1 = billingModule) {
            val success = object : BillingModule.OnSuccessListener {
                override fun onSuccess(purchase: Purchase) {
                    purchase.toProduct()?.let {
                        if (it == BillingModule.Product.AD_REMOVE)
                            adMobManager.updateIsAdViewRemoved(true)
                    }

                    showSnackBar(
                        SnackBarMessage(
                            headerMessage = "${purchase.products.first()} ${getString(R.string.main_message_success_purchase)}"
                        )
                    )
                }
            }
            val fail = object : BillingModule.OnFailListener {
                override fun onFailure(errorCode: Int) {
                    showSnackBar(
                        SnackBarMessage(
                            headerMessage = getString(R.string.main_message_fail_purchase),
                            contentMessage = getString(
                                when (errorCode) {
                                    2, 3 -> R.string.main_message_fail_unavailable
                                    5, 6 -> R.string.main_message_fail_wrong
                                    BillingClient.BillingResponseCode.USER_CANCELED -> R.string.main_message_fail_cancel
                                    BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> R.string.main_message_fail_unavailable
                                    BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> R.string.main_message_fail_already_owned
                                    else -> R.string.main_message_fail_network
                                }
                            )
                        )
                    )
                }
            }
            val ready = object : BillingModule.OnReadyListener {
                override fun onReady(billingModule: BillingModule) {
                    coroutineScope.launch(Dispatchers.Main.immediate) {
                        billingModule.queryPurchaseAsync().also { purchasedList ->
                            billingModule.approvePurchased(purchasedList)

                            if (!billingModule.isProductPurchased(
                                    product = BillingModule.Product.AD_REMOVE,
                                    purchaseList = purchasedList,
                                )
                            )
                                adMobManager.initAdView()
                        }
                    }
                }
            }

            billingModule.addBillingListener(
                onSuccess = success,
                onFailure = fail,
                onReady = ready,
            )

            onDispose {
                billingModule.removeBillingListener(
                    onSuccess = success,
                    onFailure = fail,
                    onReady = ready,
                )
            }
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

        val router = rememberRouter(navController = navController)
        val currentDestination by rememberUpdatedState(newValue = router.currentDestination)

        val layoutType by rememberUpdatedState(
            newValue = if (!currentDestination.isBarHasToBeShown())
                NavigationSuiteType.None
            else
                NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
        )

        CompositionLocalProvider(
            LocalTonalElevationEnabled provides false,
            LocalAnalyticsLoggingEvent provides ::loggingAnalyticsEvent,
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
                    BannerAd(
                        adsVisibility = !isAdViewRemoved,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = {
                            SnackBarHostCustom(
                                headerMessage = snackBarHostState.currentSnackbarData?.visuals?.message
                                    ?: "",
                                contentMessage = snackBarHostState.currentSnackbarData?.visuals?.actionLabel
                                    ?: "",
                                snackBarHostState = snackBarHostState,
                                disMissSnackBar = { snackBarHostState.currentSnackbarData?.dismiss() })
                        }
                    ) { paddingValues ->
                        NavigationGraph(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    bottom = paddingValues.calculateBottomPadding(),
                                    start = paddingValues.calculateStartPadding(LayoutDirection.Rtl),
                                    end = paddingValues.calculateStartPadding(LayoutDirection.Rtl),
                                ),
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
                        override fun onAdDismissedFullScreenContent() {
                            mRewardedAd = null
                            loadRewardedAd()
                        }

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            mRewardedAd = null
                        }

                        override fun onAdClicked() {}
                        override fun onAdImpression() {}
                        override fun onAdShowedFullScreenContent() {}
                    }
                }
            })
    }

    private fun showRewardedAd(onResult: () -> Unit, recursiveTimes: Int = 2) {
        if (recursiveTimes > 0)
            mRewardedAd?.show(this) {
                onResult()
            } ?: run {
                loadRewardedAd()
                showRewardedAd(onResult = onResult, recursiveTimes = recursiveTimes - 1)
            }
    }

    override fun onResume() {
        super.onResume()

        inAppUpdateManager.checkUpdateIsDownloaded()
        requestPermission()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}