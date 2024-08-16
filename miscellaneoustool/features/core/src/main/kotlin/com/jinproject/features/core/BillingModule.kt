package com.jinproject.features.core

import android.app.Activity
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
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import com.jinproject.features.core.BillingModule.Product.Companion.findProductById
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 결제 관리 모듈 클래스
 * @param context 결제 객체생성에 필요
 * @param lifecycleScope 수행될 코루틴 스쿠프
 * @param callback 결제 수행 콜백
 * @property isReady 결제를 수행하기에 준비된 상태인지의 여부
 */
@Stable
class BillingModule(
    private val context: Activity,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val callback: BillingCallback,
) {

    var isReady: Boolean = false

    private var _isConsumeProduct: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val consumeProduct = _isConsumeProduct.asStateFlow()

    fun updateIsConsumeProduct(bool: Boolean) = _isConsumeProduct.update { bool }

    private val purChasedUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (purchases != null) {
                    for (purchase in purchases) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            handlePurchase(
                                purchase,
                                purchase.products.first().findProductById()!!.isConsume
                            )
                        }
                    }
                }
            }

            else -> {
                callback.onFailure(billingResult.responseCode)
                _isConsumeProduct.update { false }
            }
        }
    }

    private var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(purChasedUpdatedListener)
        .enablePendingPurchases()
        .build()

    init {
        initBillingClient(3)
    }

    fun initBillingClient(maxCount: Int) {
        if (maxCount > 0) {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        lifecycleScope.launch {
                            callback.onReady()
                            isReady = true
                        }
                    } else {
                        callback.onFailure(billingResult.responseCode)
                    }
                }

                override fun onBillingServiceDisconnected() {
                    initBillingClient(maxCount - 1)
                }
            })
        }
    }

    /**
     * 구매가능한 상품이면 상품정보를 가져오는 함수
     * @param product : 상품
     * @return 상품정보 또는 구매 가능한 상품이 아닌 경우 null
     */
    suspend fun getPurchasableProducts(
        products: List<Product>,
    ): List<ProductDetails?>? {
        val productList = products.map { product ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(product.id)
                .setProductType(product.type)
                .build()
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        val productDetailsResult = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(params)
        }

        if (productDetailsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            return productDetailsResult.productDetailsList?.map { product ->
                require(product.productId.findProductById() != null) {
                    "플레이 콘솔의 상품목록에 없는 상품이거나 Product 클래스의 요소에 선언 되지 않았습니다."
                }
                when (product.productId.findProductById()!!.isConsume) {
                    true -> product
                    false -> {
                        if (queryPurchaseAsync()?.find { it.products.first() == product.productId } == null) {
                            product
                        } else
                            null
                    }
                }
            }
        }

        return null
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

    /**
     * 구매 처리
     * @param purchase 구매 상품
     * @param isConsume 소비 여부
     */
    private suspend fun handlePurchase(purchase: Purchase, isConsume: Boolean) {
        when (isConsume) {
            true -> {
                val consumeParams =
                    ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                withContext(Dispatchers.IO) {
                    billingClient.consumeAsync(consumeParams) { result, _ ->
                        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                            callback.onSuccess(purchase)
                            _isConsumeProduct.update { true }
                        } else {
                            callback.onFailure(result.responseCode)
                            _isConsumeProduct.update { false }
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
     * 구매 가져 오기
     * @return 구매한 product의 id값으로 변환된 구매 목록 반환, 구매한 것 이 없다면 null
     */
    suspend fun queryPurchaseAsync(): List<Purchase>? {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        val queryAsyncResult = billingClient.queryPurchasesAsync(params)

        return if (queryAsyncResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK)
            queryAsyncResult.purchasesList.map { record ->
                Purchase(record.originalJson, record.signature)
            }
        else
            null
    }


    /**
     * 비소비성 제품에 한하여 결제 재확인(승인)
     * @param purchaseList 구매 목록
     */
    suspend fun approvePurchased(purchaseList: List<Purchase>) {
        purchaseList.forEach { purchase ->
            if (!purchase.isAcknowledged && purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.products.first()
                    .findProductById()!!.isConsume
            ) {
                handlePurchase(
                    purchase = purchase,
                    purchase.products.first().findProductById()!!.isConsume
                )
            }
        }
    }

    /**
     * 구매 목록내에서 구매 처리가 완료되었는지 확인
     * @param purchaseList 구매 목록
     * @param productId 비교할 상품 id
     * @return 구매한적이 있으면 true, 없으면 false
     */
    fun checkPurchased(purchaseList: List<Purchase>, productId: String): Boolean =
        purchaseList.find { purchase ->
            (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) && purchase.products.contains(
                productId
            )
        } != null

    interface BillingCallback {
        suspend fun onReady()
        fun onSuccess(purchase: Purchase)
        fun onFailure(errorCode: Int)
    }

    enum class Product(val id: String, val type: String, val isConsume: Boolean) {
        AD_REMOVE("ad_remove", BillingClient.ProductType.INAPP, false),
        SUPPORT("support", BillingClient.ProductType.INAPP, true),
        SYMBOL_GM("symbol_guild_mark", BillingClient.ProductType.INAPP, true);

        companion object {
            fun String.findProductById() =
                kotlin.runCatching { entries.find { value -> value.id == this } }.getOrNull()
        }
    }
}