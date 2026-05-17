package com.top.example.billing

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class BillingHelper(
    private val activity: Activity,
    private val onPurchased: suspend (purchaseToken: String, productId: String, purchase: Purchase) -> Unit,
    private val onError: (String) -> Unit
) : PurchasesUpdatedListener {

    private val client = BillingClient.newBuilder(activity)
        .setListener(this)
        .enablePendingPurchases()
        .build()


    @Volatile private var connected: Boolean = false


    suspend fun ensureConnected(): Boolean = suspendCancellableCoroutine { cont ->

        if (connected) {
            cont.resume(true); return@suspendCancellableCoroutine
        }

        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(b: BillingResult) {
                if (!cont.isActive) return
                if (b.responseCode == BillingClient.BillingResponseCode.OK) {
                    connected = true
                    cont.resume(true)
                } else {
                    connected = false
                    cont.resume(false)
                }
            }
            override fun onBillingServiceDisconnected() {
                connected = false
                if (!cont.isActive) return
                cont.resume(false)
            }
        })
    }

    fun start(productId: String = BillingProducts.PRO_OR_ACOMP) {
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(b: BillingResult) {
                if (b.responseCode != BillingClient.BillingResponseCode.OK) {
                    connected = false
                    onError("Billing falhou: ${b.debugMessage}")
                    return
                }
                connected = true
                queryAndLaunch(productId)
            }
            override fun onBillingServiceDisconnected() {
                connected = false

            }
        })
    }

    private fun queryAndLaunch(productId: String) {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            ).build()

        client.queryProductDetailsAsync(params) { result, detailsList ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                onError("Falha ao consultar produtos (${result.responseCode}): ${result.debugMessage}")
                return@queryProductDetailsAsync
            }

            val details = detailsList.firstOrNull()
                ?: return@queryProductDetailsAsync onError("Produto não encontrado no Play Console: $productId")

            val offerToken = details.subscriptionOfferDetails?.firstOrNull()?.offerToken
                ?: return@queryProductDetailsAsync onError("Offer token ausente. Crie Base plan + Offer no Play Console para $productId")

            val flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(
                    listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(details)
                            .setOfferToken(offerToken)
                            .build()
                    )
                )
                .build()

            val res = client.launchBillingFlow(activity, flowParams)


            if (res.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                activity.lifecycleScopeOrNull()?.launch {
                    recoverOwnedSubscriptions(productId) { token, prod, purchase ->
                        onPurchased(token, prod, purchase)
                    }
                }
            } else if (res.responseCode != BillingClient.BillingResponseCode.OK) {
                onError("Não foi possível abrir o fluxo (${res.responseCode}): ${res.debugMessage}")
            }
        }
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (!purchases.isNullOrEmpty()) {
                    purchases.forEach { p ->
                        if (p.purchaseState == Purchase.PurchaseState.PURCHASED) {
                            activity.lifecycleScopeOrNull()?.launch {
                                try {
                                    onPurchased(p.purchaseToken, p.products.firstOrNull() ?: "", p)
                                } catch (e: Exception) {
                                    onError("Falha pós-compra: ${e.message ?: "erro"}")
                                }
                            }
                        }
                    }
                } else {

                    onError("Fluxo concluído, mas nenhuma compra retornada.")
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {

                onError("")
            }
            else -> {
                onError("Compra falhou (${result.responseCode}): ${result.debugMessage}")
            }
        }
    }


    suspend fun recoverOwnedSubscriptions(
        productIdFilter: String?,
        onEachRecovered: suspend (purchaseToken: String, productId: String, purchase: Purchase) -> Unit
    ) {

        if (!ensureConnected()) return

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        val result = client.queryPurchasesAsync(params)
        val list = result.purchasesList.orEmpty()

        list.forEach { p ->
            val prodId = p.products.firstOrNull().orEmpty()
            if (productIdFilter == null || productIdFilter == prodId) {
                if (p.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    onEachRecovered(p.purchaseToken, prodId, p)
                }
            }
        }
    }


    suspend fun acknowledgeIfNeeded(purchase: Purchase): Boolean = withContext(Dispatchers.IO) {
        if (purchase.isAcknowledged) return@withContext true
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        val res = client.acknowledgePurchase(params)
        res.responseCode == BillingClient.BillingResponseCode.OK
    }


    suspend fun acknowledgeToken(purchaseToken: String): Boolean = withContext(Dispatchers.IO) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()
        val res = client.acknowledgePurchase(params)
        res.responseCode == BillingClient.BillingResponseCode.OK
    }
}


fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
fun Activity.lifecycleScopeOrNull(): LifecycleCoroutineScope? =
    (this as? ComponentActivity)?.lifecycleScope
