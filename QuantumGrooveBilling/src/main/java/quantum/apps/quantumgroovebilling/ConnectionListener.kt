package quantum.apps.quantumgroovebilling

interface ConnectionListener {
    fun onConnected(status: Boolean, billingResponseCode: Int)
}