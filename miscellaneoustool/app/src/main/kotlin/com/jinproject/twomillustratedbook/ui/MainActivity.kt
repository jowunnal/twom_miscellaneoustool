package com.jinproject.twomillustratedbook.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jinproject.features.core.BillingModule
import com.jinproject.features.core.base.CommonDialogFragment
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBillingModule()
        initTopBar()
        initBottomNavigationBar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        getLatestVersion()
    }

    private fun initTopBar() {
        setSupportActionBar(binding.bookToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(com.jinproject.design_ui.R.drawable.ic_arrow_left)
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

    private fun getLatestVersion() {
        val ref = FirebaseDatabase.getInstance().getReference("version")

        ref.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        packageManager.getPackageInfo(
                            packageName,
                            PackageManager.PackageInfoFlags.of(0L)
                        )
                    else
                        packageManager.getPackageInfo(
                            packageName,
                            0
                        )

                    if (snapshot.value.toString() != packageInfo.versionName)
                        CommonDialogFragment.show(
                            fragmentManager = supportFragmentManager,
                            title = getString(com.jinproject.design_ui.R.string.new_version_message),
                            message = null,
                            positiveButtonText = getString(com.jinproject.design_ui.R.string.new_version_button),
                            negativeButtonText = "",
                            listener = object : CommonDialogFragment.Listener() {
                                override fun onPositiveButtonClick(value: String) {
                                    requireLatestVersionOnMarket()
                                }
                            },
                            enabled = false
                        )
                }

                override fun onCancelled(error: DatabaseError) {}

            }
        )
    }

    private fun requireLatestVersionOnMarket() {
        val uriString = "market://details?id=$packageName"

        startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                setPackage("com.android.vending")
                data = Uri.parse(uriString)
                putExtra("overlay", true)
                putExtra("callerId", packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
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