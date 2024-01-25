package quantum.apps.quantumgroovebilling

import android.app.Activity
import android.content.Context
import kotlinx.coroutines.DelicateCoroutinesApi

class BillingConnector @JvmOverloads constructor(
    context: Context,
    nonConsumableKeys: List<String> = emptyList(),
    consumableKeys: List<String> = emptyList(),
    subscriptionKeys: List<String> = emptyList(),
    enableLogging: Boolean = false
) {

    private var mBillingService: IBillingService? = null

    init {
        if (mBillingService==null) {
            val contextLocal = context.applicationContext ?: context
            mBillingService = BillingService(contextLocal, nonConsumableKeys, consumableKeys, subscriptionKeys)
            getBillingService().init()
            getBillingService().enableDebugLogging(enableLogging)
        }
    }

    fun setConnectionListener(connectionListener: ConnectionListener) {
        getBillingService().addConnectionListener(connectionListener)
    }

    fun removeConnectionListener(connectionListener: ConnectionListener) {
        getBillingService().removeConnectionListener(connectionListener)
    }

    fun setPurchaseListener(purchaseListener: PurchaseListener) {
        getBillingService().setPurchaseListener(purchaseListener)
    }

    fun removePurchaseListener(purchaseListener: PurchaseListener) {
        getBillingService().removePurchaseListener(purchaseListener)
    }

    fun setSubscriptionListener(subscriptionListener: SubscriptionListener) {
        getBillingService().setSubscriptionListener(subscriptionListener)
    }

    fun removeSubscriptionListener(subscriptionListener: SubscriptionListener) {
        getBillingService().removeSubscriptionListener(subscriptionListener)
    }

    fun purchase(activity: Activity, sku: String) {
        getBillingService().buy(activity, sku)
    }

    fun subscribe(activity: Activity, sku: String) {
        getBillingService().subscribe(activity, sku)
    }

    fun unsubscribe(activity: Activity, sku: String) {
        getBillingService().unsubscribe(activity, sku)
    }

    fun destroy() {
        getBillingService().close()
    }

    fun getSubPriceByTag(tag:String){
        getBillingService()
    }

    private fun getBillingService(): IBillingService {
        return mBillingService ?: let {
            throw RuntimeException("Call BillingConnector to initialize billing service")
        }
    }
}
