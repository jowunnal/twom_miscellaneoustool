package com.jinproject.twomillustratedbook.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigator
import androidx.navigation.FloatingWindow
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.android.billingclient.api.Purchase
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.base.CommonDialogFragment
import com.jinproject.features.core.listener.BottomNavigationController
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationController {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

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
    private var billingCallback: OnBillingCallback? = null
    fun setBillingCallback(listener: OnBillingCallback) {
        billingCallback = listener
    }

    interface OnBillingCallback {
        fun onSuccess(purchase: Purchase)
        fun onFailure(errorCode: Int)
    }

    lateinit var billingModule: BillingModule

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let { notNullIntent ->
            when (notNullIntent.getStringExtra("screen")) {
                "alarm" -> {
                    val navigationBarView =
                        (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment)
                    val navController = navigationBarView.navController

                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(
                            navController.graph.findStartDestination().id,
                            inclusive = false,
                            saveState = true
                        )
                        .setEnterAnim(androidx.navigation.ui.R.animator.nav_default_enter_anim)
                        .setExitAnim(androidx.navigation.ui.R.animator.nav_default_exit_anim)
                        .setPopEnterAnim(androidx.navigation.ui.R.animator.nav_default_pop_enter_anim)
                        .setPopExitAnim(androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
                        .build()

                    if (navController.currentBackStackEntry?.destination?.id != R.id.navi_alarm) {
                        navController.navigate(R.id.navi_alarm, null, navOptions)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initBillingModule()
        initTopBar()
        initBottomNavigationBar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        checkNewUpdateIsAvailable()
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
                        binding.adView.visibility = View.GONE
                }
            }
        }

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    requireInstallingNotUpdatedYet()
                }
            }
    }

    private fun initTopBar() {
        setSupportActionBar(binding.bookToolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(com.jinproject.design_ui.R.drawable.ic_arrow_left)
        }
    }

    private fun initBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.apply {
            setUpBottomNavigationDestinationChangedListener(navController)
            setOnItemSelectedListener { item ->
                setUpBottomNavigationItemClickedListener(item, navController)
            }
            labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED
        }
    }

    private fun setUpBottomNavigationDestinationChangedListener(navController: NavController) {
        val weakReference = WeakReference(binding.bottomNavigationView)
        navController.addOnDestinationChangedListener(
            object : NavController.OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {
                    val view = weakReference.get()
                    if (view == null) {
                        navController.removeOnDestinationChangedListener(this)
                        return
                    }
                    if (destination is FloatingWindow) {
                        return
                    }
                    view.menu.forEach { item ->
                        if (destination.hierarchy.any { it.id == item.itemId }) {
                            item.isChecked = true
                        }
                    }
                }
            }
        )
    }

    private fun setUpBottomNavigationItemClickedListener(item: MenuItem, navController: NavController): Boolean {
        val builder = NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(true)
        if (
            navController.currentDestination!!.parent!!.findNode(item.itemId)
                    is ActivityNavigator.Destination
        ) {
            builder
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
        } else {
            builder
                .setEnterAnim(androidx.navigation.ui.R.animator.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.animator.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.animator.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.animator.nav_default_pop_exit_anim)
        }
        if (item.order and Menu.CATEGORY_SECONDARY == 0) {
            builder.setPopUpTo(
                navController.graph.hierarchy.first().id,
                inclusive = true,
                saveState = true
            )
        }

        val options = builder.build()
        return try {
            navController.navigate(item.itemId, null, options)
            navController.currentDestination?.hierarchy?.any { it.id == item.itemId } == true
        } catch (e: IllegalArgumentException) {
            false
        }
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
                                initAdView()
                            }
                        }
                }

                override fun onSuccess(purchase: Purchase) {
                    billingCallback?.onSuccess(purchase)
                }

                override fun onFailure(errorCode: Int) {
                    billingCallback?.onFailure(errorCode)
                }
            })
    }

    private fun initAdView() {
        MobileAds.initialize(this) { }
        val adRequest = AdRequest.Builder().build()
        binding.apply {
            adView.loadAd(adRequest)
            adView.visibility = View.VISIBLE
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {}

                override fun onAdFailedToLoad(adError: LoadAdError) {}

                override fun onAdOpened() {}

                override fun onAdClicked() {}

                override fun onAdClosed() {}
            }
        }
    }

    private val requestInAppUpdatingLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                installUpdatedListener = InstallStateUpdatedListener { state ->
                    when(state.installStatus()) {
                        InstallStatus.DOWNLOADED -> {
                            requireInstallingNotUpdatedYet()
                        }
                        else -> {}
                    }
                }
                installUpdatedListener?.let { listener ->
                    appUpdateManager.registerListener(listener)
                    Snackbar.make(this.window.decorView.rootView,getString(com.jinproject.design_ui.R.string.updating_new_version),Snackbar.LENGTH_LONG).show()
                }
            } else {
                sendMessageIfDenyUpdate()
            }
        }

    private fun checkNewUpdateIsAvailable() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                requestInAppUpdate()
            }
        }
    }

    private var installUpdatedListener: InstallStateUpdatedListener? = null

    private fun requestInAppUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                requestInAppUpdatingLauncher,
                AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
            )
        }
    }

    private fun requireInstallingNotUpdatedYet() {
        installUpdatedListener?.let { listener ->
            appUpdateManager.unregisterListener(listener)
            installUpdatedListener = null
        }

        CommonDialogFragment.show(
            fragmentManager = supportFragmentManager,
            title = getString(com.jinproject.design_ui.R.string.new_version_message),
            message = null,
            positiveButtonText = getString(com.jinproject.design_ui.R.string.new_version_positive_button),
            negativeButtonText = getString(com.jinproject.design_ui.R.string.new_version_negative_button),
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

    private fun sendMessageIfDenyUpdate() {
        Snackbar.make(this.window.decorView.rootView,getString(com.jinproject.design_ui.R.string.deny_new_version_update),Snackbar.LENGTH_LONG).show()

        installUpdatedListener?.let { listener ->
            appUpdateManager.unregisterListener(listener)
            installUpdatedListener = null
        }
    }

    override fun hideBottomNavigation() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    override fun showBottomNavigation() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}