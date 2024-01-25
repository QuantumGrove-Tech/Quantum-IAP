package my.apps.billing

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import quantum.apps.quantumgroovebilling.BillingConnector
import quantum.apps.quantumgroovebilling.ConnectionListener
import quantum.apps.quantumgroovebilling.DataWrappers
import quantum.apps.quantumgroovebilling.PurchaseListener
import quantum.apps.quantumgroovebilling.SubscriptionListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connector = BillingConnector(
            this,
            nonConsumableKeys = arrayListOf("silver", "gold", "premium"),
            subscriptionKeys = arrayListOf("monthly", "yearly"),
            enableLogging = true
        )

        connector.setConnectionListener(object : ConnectionListener {
            override fun onConnected(status: Boolean, billingResponseCode: Int) {

            }
        })

        connector.setSubscriptionListener(object : SubscriptionListener {
            override fun onSubscriptionRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
                // will be triggered upon fetching owned subscription upon initialization
            }

            override fun onSubscriptionPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
                when (purchaseInfo.sku) {
                    "subscription" -> {

                    }

                    "yearly" -> {

                    }
                }
            }

            override fun onPricesUpdated(iapKeyPrices: Map<String, List<DataWrappers.ProductDetails>>) {
                // list of available products will be received here, so you can update UI with prices if needed
            }

            override fun onPurchaseFailed(purchaseInfo: DataWrappers.PurchaseInfo?, billingResponseCode: Int?) {
                // will be triggered whenever subscription purchase is failed
            }
        })

        connector.setPurchaseListener(object : PurchaseListener {
            override fun onPricesUpdated(iapKeyPrices: Map<String, List<DataWrappers.ProductDetails>>) {
                // list of available products will be received here, so you can update UI with prices if needed
            }

            override fun onProductPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
                when (purchaseInfo.sku) {
                    "silver" -> {
                        purchaseInfo.orderId
                    }

                    "gold" -> {

                    }

                    "premium" -> {

                    }
                }
            }

            override fun onProductRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
                // will be triggered fetching owned products using IapConnector;
            }

            override fun onPurchaseFailed(purchaseInfo: DataWrappers.PurchaseInfo?, billingResponseCode: Int?) {
                // will be triggered whenever a product purchase is failed
                Toast.makeText(applicationContext, "Your purchase has been failed", Toast.LENGTH_SHORT).show()
            }

        })
    }
}