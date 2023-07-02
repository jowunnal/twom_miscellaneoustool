package com.jinproject.twomillustratedbook.ui.screen.compose.navigation

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.LifecycleCoroutineScope
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchaseHistory
import com.jinproject.twomillustratedbook.ui.screen.compose.navigation.BillingModule.Product.Companion.findProductById
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Stable
class BillingModule(
    private val context: Activity,
    private val lifeCycleScope: LifecycleCoroutineScope,
    private val callback: BillingCallback
) {

    private val _purchasableProducts = MutableStateFlow(listOf<ProductDetails>())
    val purchasableProducts get() = _purchasableProducts.asStateFlow()


    private val purChasedUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        when(billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if(purchases != null) {
                    for(purchase in purchases) {
                        lifeCycleScope.launch {
                            handlePurchase(purchase, purchase.products.first().findProductById()!!.isConsume)
                        }
                    }
                }
            }
            else -> {
                callback.onFailure(billingResult.responseCode)
            }
        }
    }

    private var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(purChasedUpdatedListener)
        .enablePendingPurchases()
        .build()

    init {
        billingClient.startConnection(object: BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    lifeCycleScope.launch(Dispatchers.IO) {
                        getPurchasableProducts()
                    }
                } else {
                    callback.onFailure(billingResult.responseCode)
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.e("test","disconnected")
            }
        })
    }

    /**
     * 구매 가능한 상품 목록을 조회
     * @param productId: 상품 id
     * @param productType: 상품 type
     * @param onDisplayed: 조회된 상품을 게시하기 위한 callback
     */
    suspend fun getPurchasableProducts() {
        val productList = ArrayList<QueryProductDetailsParams.Product>().apply {
            Product.values().forEach { product ->
                add(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(product.id)
                        .setProductType(product.type)
                        .build()
                )
            }
        }

        val params = QueryProductDetailsParams.newBuilder().apply {
            setProductList(productList)
        }

        val productDetailsResult = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(params.build())
        }

        _purchasableProducts.update {
            if(productDetailsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                (productDetailsResult.productDetailsList?.filter { productDetail ->
                    kotlin.runCatching {
                        productDetail.productId.findProductById()
                    }.getOrNull() != null
                } ?: emptyList())
            } else
                emptyList()
        }
    }

    /**
     * 구매 수행
     * @param productDetails : 구매 가능한 상품
     */

    fun purchase(productDetails: ProductDetails) {
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(context, billingFlowParams)
    }

    private suspend fun handlePurchase(purchase: Purchase, isConsume: Boolean) {
        when(isConsume) {
            true -> {
                val consumeParams =
                    ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                withContext(Dispatchers.IO) {
                    billingClient.consumeAsync(consumeParams) { result, token ->
                        if(result.responseCode == BillingClient.BillingResponseCode.OK) {
                            callback.onSuccess(purchase)
                        } else {
                            callback.onFailure(result.responseCode)
                        }
                    }
                }
            }
            false -> {
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)

                    withContext(Dispatchers.IO) {
                        billingClient.acknowledgePurchase(acknowledgePurchaseParams.build()) { billingResult ->
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                callback.onSuccess(purchase)
                            } else {
                                callback.onFailure(billingResult.responseCode)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
    구매 기록 조회
     */
    fun queryPurchase() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)

        billingClient.queryPurchasesAsync(params.build()) { billingResult, purchaseList ->
            if(billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                purchaseList.forEach { purchase ->
                    if(purchase.isAcknowledged && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        lifeCycleScope.launch {
                            handlePurchase(purchase = purchase, purchase.products.first().findProductById()!!.isConsume)
                        }
                    }
                    if(purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        _purchasableProducts.update { it.filter { product -> product.productId !in purchase.products } }
                    }
                }
            }
        }
    }

    suspend fun queryPurchaseLatest() {
        val params = QueryPurchaseHistoryParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)

        return withContext(Dispatchers.IO) { billingClient.queryPurchaseHistory(params.build()) }
    }

    interface BillingCallback {
        fun onSuccess(purchase: Purchase)
        fun onFailure(errorCode: Int)
    }

    enum class Product(val id: String, val type: String, val isConsume: Boolean) {
        AD_REMOVE("ad_remove", BillingClient.ProductType.INAPP, false),
        SUPPORT("support", BillingClient.ProductType.INAPP, true);

        companion object {
            fun String.findProductById() = values().find { value -> value.id == this }
        }
    }
}