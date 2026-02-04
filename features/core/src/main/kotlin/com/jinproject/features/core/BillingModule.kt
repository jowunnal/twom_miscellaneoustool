package com.jinproject.features.core

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Immutable
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import com.jinproject.features.core.BillingModule.Product.Companion.findProductById
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CopyOnWriteArraySet

/**
 * 결제 관리 모듈 클래스
 *
 * @param activity 결제에 필요한 컨텍스트, 내부적으로 AppicationContext 참조하여 사용
 * @param coroutineScope 코루틴 호환하여 실행될 코루틴 스쿠프
 * @param callback 결제 실행 후 준비,성공,실패 콜백
 * @property isReady 결제를 수행하기에 준비된 상태인지의 여부
 */
@Immutable
class BillingModule(
    private val activity: Activity,
    private val coroutineScope: CoroutineScope,
) {
    var isReady: Boolean = false

    private val purChasedUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (purchases != null) {
                    for (purchase in purchases) {
                        coroutineScope.launch(Dispatchers.IO) {
                            handlePurchase(
                                purchase = purchase,
                                isConsumable = purchase.products.first()
                                    .findProductById()!!.isConsumable
                            )
                        }
                    }
                }
            }

            else -> {
                failListener.call(billingResult.responseCode)
            }
        }
    }

    private val billingClient: BillingClient = BillingClient.newBuilder(activity)
        .setListener(purChasedUpdatedListener)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .enableAutoServiceReconnection()
        .build()

    init {
        initBillingClient()
    }

    /**
     * 구글 결제 백엔드와의 연결 활성화
     */
    fun initBillingClient() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    readyListener.call(this@BillingModule)
                    isReady = true
                } else {
                    failListener.call(billingResult.responseCode)
                    isReady = false
                }
            }

            override fun onBillingServiceDisconnected() {
                isReady = false
            }
        })
    }

    private val successListener: BillingListener<Purchase> = BillingListener()
    private val failListener: BillingListener<Int> = BillingListener()
    private val readyListener: BillingListener<BillingModule> = BillingListener()

    /**
     * 결제 플로우 리스너 등록
     */
    fun addBillingListener(
        onSuccess: OnSuccessListener? = null,
        onFailure: OnFailListener? = null,
        onReady: OnReadyListener? = null,
    ) {
        onSuccess?.let {
            successListener.addListener(it)
        }
        onFailure?.let {
            failListener.addListener(it)
        }
        onReady?.let {
            readyListener.addListener(it)
        }
    }

    /**
     * 결제 플로우 리스너 제거
     */
    fun removeBillingListener(
        onSuccess: OnSuccessListener? = null,
        onFailure: OnFailListener? = null,
        onReady: OnReadyListener? = null,
    ) {
        onSuccess?.let {
            successListener.removeListener(it)
        }
        onFailure?.let {
            failListener.removeListener(it)
        }
        onReady?.let {
            readyListener.removeListener(it)
        }
    }

    /**
     * 구매가능한 상품이면 상품정보를 가져오는 함수
     *
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
                val availableProduct = product.productId.findProductById()

                require(availableProduct != null) {
                    "플레이 콘솔의 상품목록에 없는 상품이거나 Product 클래스의 요소에 선언 되지 않았습니다."
                }

                if (availableProduct.isConsumable)
                    product
                else {
                    val purchasedHistory = queryPurchaseAsync()

                    purchasedHistory.find { it.products.contains(product.productId) }?.let {
                        null
                    } ?: product
                }
            }
        }

        return null
    }

    /**
     * 결제 플로우 실행
     *
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

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    /**
     * 구매 처리
     *
     * 구매된 상태에 한하여,
     * 소비성 제품이면, 소비 처리후 콜백 실행
     * 소비성 제품이 아니면, 승인되지 않은 경우, 승인 처리후 콜백 실행
     *
     * @param purchase 구매 상품
     * @param isConsumable 소비성 제품 인지 아닌지
     */
    private suspend fun handlePurchase(purchase: Purchase, isConsumable: Boolean) =
        withContext(Dispatchers.IO) {
            if (purchase.isPurchased()) {
                if (isConsumable) {
                    val consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    billingClient.consumeAsync(consumeParams) { result, _ ->
                        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                            successListener.call(purchase)
                        } else {
                            failListener.call(result.responseCode)
                        }
                    }
                } else {
                    if (!purchase.isAcknowledged) {
                        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.purchaseToken)

                        billingClient.acknowledgePurchase(acknowledgePurchaseParams.build()) { result ->
                            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                                successListener.call(purchase)
                            } else {
                                failListener.call(result.responseCode)
                            }
                        }
                    }
                }
            }
        }

    /**
     * 구매 처리 수동 실행
     *
     * @return 구매 목록 반환
     */
    suspend fun queryPurchaseAsync(): List<Purchase> {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)

        val queryAsyncResult = withContext(Dispatchers.IO) {
            billingClient.queryPurchasesAsync(params.build())
        }

        return if (queryAsyncResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK)
            queryAsyncResult.purchasesList
        else
            emptyList()
    }


    /**
     * 구매 목록 내에서, 결제 승인 처리
     * @param purchaseList 구매 목록
     */
    suspend fun approvePurchased(purchaseList: List<Purchase>) {
        purchaseList.forEach { purchase ->
            handlePurchase(
                purchase = purchase,
                isConsumable = purchase.products.first().findProductById()!!.isConsumable,
            )
        }
    }

    /**
     * 구매 목록내에서 특정 상품이 구매 와 승인 되었는지 확인
     * @param purchaseList 구매 목록
     * @param productId 비교할 상품 id
     * @return 구매 후 승인 되었다면 true, 없으면 false
     */
    suspend fun isProductPurchased(product: Product): Boolean {
        return queryPurchaseAsync().filter { purchase ->
            purchase.isPurchasedAndAcknowledged()
        }.find { purchase ->
            purchase.products.contains(product.id)
        } != null
    }

    /**
     * 구매 목록내에서 특정 상품이 구매 와 승인 되었는지 확인
     * @param purchaseList 구매 목록
     * @param productId 비교할 상품 id
     * @return 구매 후 승인 되었다면 true, 없으면 false
     */
    fun isProductPurchased(product: Product, purchaseList: List<Purchase>): Boolean {
        return purchaseList.filter { purchase ->
            purchase.isPurchasedAndAcknowledged()
        }.find { purchase ->
            purchase.products.contains(product.id)
        } != null
    }

    interface BillingCallback<T> {
        fun call(c: T)
    }

    interface OnReadyListener : BillingCallback<BillingModule> {
        override fun call(c: BillingModule) {
            onReady(c)
        }

        fun onReady(billingModule: BillingModule)
    }

    interface OnSuccessListener : BillingCallback<Purchase> {
        override fun call(c: Purchase) {
            onSuccess(c)
        }

        fun onSuccess(purchase: Purchase)
    }

    interface OnFailListener : BillingCallback<Int> {
        override fun call(c: Int) {
            onFailure(c)
        }

        fun onFailure(errorCode: Int)
    }

    class BillingListener<T> {
        private val callbacks: CopyOnWriteArraySet<BillingCallback<T>> = CopyOnWriteArraySet()

        fun addListener(c: BillingCallback<T>) {
            callbacks.add(c)
        }

        fun removeListener(c: BillingCallback<T>) {
            callbacks.remove(c)
        }

        fun call(c: T) {
            callbacks.onEach { it.call(c) }
        }
    }

    enum class Product(val id: String, val type: String, val isConsumable: Boolean) {
        AD_REMOVE("ad_remove", BillingClient.ProductType.INAPP, false),
        SUPPORT("support", BillingClient.ProductType.INAPP, true),
        SYMBOL_GM("symbol_guild_mark", BillingClient.ProductType.INAPP, true),
        AI_TOKEN("ai_token", BillingClient.ProductType.INAPP, true);

        companion object {
            fun String.findProductById() =
                kotlin.runCatching { entries.find { value -> value.id == this } }.getOrNull()
        }
    }
}

/**
 * @return 구매는 완료됬지만, 승인 되지 않은 경우 true 아니면 false
 */
fun Purchase.isPurchasedButAcknowledged(): Boolean =
    isPurchased() && !isAcknowledged

/**
 * @return 구매와 승인이 모두 완료된 경우 true 아니면 false
 */
fun Purchase.isPurchasedAndAcknowledged(): Boolean =
    isPurchased() && isAcknowledged

fun Purchase.isPurchased(): Boolean = purchaseState == Purchase.PurchaseState.PURCHASED

fun Purchase.toProduct(): BillingModule.Product? = products.first().findProductById()