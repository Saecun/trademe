package nz.co.trademe.techtest.util

class Networking {
    companion object {
        // 20 seconds is long enough for the sandbox server to respond
        // perhaps if we wanted this variable for production servers,
        // we could define it in the BuildConfig
        const val TIMEOUT_SECONDS: Long = 20
    }
}