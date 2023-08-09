package com.jinproject.twomillustratedbook.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.billingclient.api.Purchase
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationBarView
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.listener.BottomNavigationController
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationController {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result.not()) {
            Toast.makeText(
                applicationContext,
                getString(R.string.authority_alarm_failure),
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
                        .setEnterAnim(R.animator.nav_default_enter_anim)
                        .setExitAnim(R.animator.nav_default_exit_anim)
                        .setPopEnterAnim(R.animator.nav_default_pop_enter_anim)
                        .setPopExitAnim(R.animator.nav_default_pop_exit_anim)
                        .build()

                    if (navController.currentBackStackEntry?.destination?.id != R.id.navigationFragment) {
                        navController.navigate(R.id.navigationFragment, null, navOptions)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (billingModule.isReady) {
            billingModule.queryPurchase { purchaseList ->
                billingModule.approvePurchased(purchaseList = purchaseList)

                if (!billingModule.checkPurchased(
                        purchaseList = purchaseList,
                        productId = "ad_remove"
                    )
                )
                    binding.adView.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBillingModule()
        initTopBar()
        initBottomNavigationBar()
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU)
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun initTopBar() {
        setSupportActionBar(binding.bookToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
    }

    private fun initBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.bottomNavigationView.apply {
            setupWithNavController(navHostFragment.navController)
            labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED
        }
    }

    private fun initBillingModule() {
        billingModule = BillingModule(
            this,
            lifeCycleScope = lifecycleScope,
            object : BillingModule.BillingCallback {
                override fun onReady() {
                    lifecycleScope.launch {
                        billingModule.getPurchasableProducts {
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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun hideBottomNavigation() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    override fun showBottomNavigation() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}