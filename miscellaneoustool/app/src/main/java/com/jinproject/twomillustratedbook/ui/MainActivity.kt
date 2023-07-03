package com.jinproject.twomillustratedbook.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.billingclient.api.Purchase
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationBarView
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.ActivityMainBinding
import com.jinproject.twomillustratedbook.ui.screen.compose.navigation.BillingModule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result.not()) {
            Toast.makeText(applicationContext, getString(R.string.authority_alarm_failure), Toast.LENGTH_LONG).show()
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

    override fun onResume() {
        if(billingModule.isReady) {
            billingModule.queryPurchase { purchaseList ->
                billingModule.approvePurchased(purchaseList = purchaseList)
            }
        }
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBillingModule()
        initTopBar()
        initBottomNavigationBar()
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU)
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun initTopBar() {
        setSupportActionBar(binding.bookToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
    }

    private fun initBottomNavigationBar() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.bottomNavigationView.apply {
            setupWithNavController(navHostFragment.navController)
            labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED
        }
    }

    private fun initBillingModule() {
        billingModule = BillingModule(this, lifeCycleScope = lifecycleScope,object: BillingModule.BillingCallback {
            override fun onReady() {
                lifecycleScope.launch(Dispatchers.IO) {
                    billingModule.getPurchasableProducts()
                    billingModule.queryPurchase { purchaseList ->
                        purchaseList.forEach {
                            Log.d("test", "${it.products}")
                        }
                        billingModule.approvePurchased(purchaseList = purchaseList)
                        if(billingModule.checkPurchased(purchaseList = purchaseList, productId = "ad_remove"))
                            initAdView()

                        val purchasedProductIds = purchaseList.distinctBy { it.products }.map { it.products.first() }
                        val purchasableProducts = billingModule.purchasableProducts.toList()
                        billingModule.purchasableProducts.clear()
                        billingModule.purchasableProducts.addAll(
                            purchasableProducts.filter { purchasableProduct ->
                                purchasableProduct.productId !in purchasedProductIds
                            }
                        )
                    }
                }
            }

            override fun onSuccess(purchase: Purchase) {
                billingCallback?.onSuccess(purchase)
            }

            override fun onFailure(errorCode: Int) {
                Log.e("test","error : $errorCode")
                billingCallback?.onFailure(errorCode)
            }
        })
    }

    private fun initAdView() {
        MobileAds.initialize(this) { }
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.adView.visibility = View.VISIBLE
        binding.adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}