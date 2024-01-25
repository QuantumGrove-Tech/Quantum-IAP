package quantum.apps.quantumgroovebilling

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.annotation.CallSuper

abstract class IBillingService {

    private val purchaseListener: MutableList<PurchaseListener> = mutableListOf()
    private val subscriptionListeners: MutableList<SubscriptionListener> = mutableListOf()
    private val connectionListener: MutableList<ConnectionListener> = mutableListOf()

    fun addConnectionListener(connectionListener: ConnectionListener) {
        this.connectionListener.add(connectionListener)
    }

    fun removeConnectionListener(connectionListener: ConnectionListener) {
        this.connectionListener.remove(connectionListener)
    }

    fun setPurchaseListener(purchaseListener: PurchaseListener) {
        this.purchaseListener.add(purchaseListener)
    }

    fun removePurchaseListener(purchaseListener: PurchaseListener) {
        this.purchaseListener.remove(purchaseListener)
    }

    fun setSubscriptionListener(subscriptionListeners: SubscriptionListener) {
        this.subscriptionListeners.add(subscriptionListeners)
    }

    fun removeSubscriptionListener(subscriptionListeners: SubscriptionListener) {
        this.subscriptionListeners.remove(subscriptionListeners)
    }

    /**
     * @param purchaseInfo Product specifier
     * @param isRestore Flag indicating whether it's a fresh purchase or restored product
     */
    fun productOwned(purchaseInfo: DataWrappers.PurchaseInfo, isRestore: Boolean) {
        findUiHandler().post {
            productOwnedInternal(purchaseInfo, isRestore)
        }
    }

    private fun productOwnedInternal(purchaseInfo: DataWrappers.PurchaseInfo, isRestore: Boolean) {
        for (purchaseServiceListener in purchaseListener) {
            if (isRestore) {
                purchaseServiceListener.onProductRestored(purchaseInfo)
            } else {
                purchaseServiceListener.onProductPurchased(purchaseInfo)
            }
        }
    }

    /**
     * @param purchaseInfo Subscription specifier
     * @param isRestore Flag indicating whether it's a fresh purchase or restored subscription
     */
    fun subscriptionOwned(purchaseInfo: DataWrappers.PurchaseInfo, isRestore: Boolean) {
        findUiHandler().post {
            subscriptionOwnedInternal(purchaseInfo, isRestore)
        }
    }

    private fun subscriptionOwnedInternal(purchaseInfo: DataWrappers.PurchaseInfo, isRestore: Boolean) {
        for (subscriptionServiceListener in subscriptionListeners) {
            if (isRestore) {
                subscriptionServiceListener.onSubscriptionRestored(purchaseInfo)
            } else {
                subscriptionServiceListener.onSubscriptionPurchased(purchaseInfo)
            }
        }
    }

    fun isBillingClientConnected(status: Boolean, responseCode: Int) {
        findUiHandler().post {
            for (billingServiceListener in connectionListener) {
                billingServiceListener.onConnected(status, responseCode)
            }
        }
    }

    fun updatePrices(iapKeyPrices: Map<String, List<DataWrappers.ProductDetails>>) {
        findUiHandler().post {
            updatePricesInternal(iapKeyPrices)
        }
    }

    private fun updatePricesInternal(iapKeyPrices: Map<String, List<DataWrappers.ProductDetails>>) {
        for (billingServiceListener in purchaseListener) {
            billingServiceListener.onPricesUpdated(iapKeyPrices)
        }
        for (billingServiceListener in subscriptionListeners) {
            billingServiceListener.onPricesUpdated(iapKeyPrices)
        }
    }

    fun updateFailedPurchases(purchaseInfo: List<DataWrappers.PurchaseInfo>? = null, billingResponseCode: Int? = null) {
        purchaseInfo?.forEach {
            updateFailedPurchase(it, billingResponseCode)
        } ?: updateFailedPurchase()
    }

    fun updateFailedPurchase(purchaseInfo: DataWrappers.PurchaseInfo? = null, billingResponseCode: Int? = null) {
        findUiHandler().post {
            updateFailedPurchasesInternal(purchaseInfo, billingResponseCode)
        }
    }

    private fun updateFailedPurchasesInternal(purchaseInfo: DataWrappers.PurchaseInfo? = null, billingResponseCode: Int? = null) {
        for (billingServiceListener in purchaseListener) {
            billingServiceListener.onPurchaseFailed(purchaseInfo, billingResponseCode)
        }
        for (billingServiceListener in subscriptionListeners) {
            billingServiceListener.onPurchaseFailed(purchaseInfo, billingResponseCode)
        }
    }

    abstract fun init()
    abstract fun buy(activity: Activity, sku: String)
    abstract fun subscribe(activity: Activity, sku: String)
    abstract fun unsubscribe(activity: Activity, sku: String)
    abstract fun enableDebugLogging(enable: Boolean)

    @CallSuper
    open fun close() {
        subscriptionListeners.clear()
        purchaseListener.clear()
        connectionListener.clear()
    }
}

fun findUiHandler(): Handler {
    return Handler(Looper.getMainLooper())
}