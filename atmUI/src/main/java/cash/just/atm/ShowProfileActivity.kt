package cash.just.atm

import android.net.Uri
import android.os.Bundle
import android.webkit.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContentProviderCompat.requireContext
import cash.just.atm.base.AtmFlowActivity
import cash.just.sdk.CashSDK
import timber.log.Timber

class ShowProfileActivity  : AtmFlowActivity() {

    private lateinit var webView: WebView
    var builder = CustomTabsIntent.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)

        webView = findViewById(R.id.webview)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.allowContentAccess = true


        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        val customTabsIntent :CustomTabsIntent  = builder.build()
        customTabsIntent.launchUrl(applicationContext, Uri.parse("https://cash-dev.coinsquareatm.com/external#key=${CashSDK.getSession()}&mode=kyc"))

        Timber.d(CashSDK.getSession())
        webView.loadUrl("https://cash-dev.coinsquareatm.com/external#key=${CashSDK.getSession()}&mode=kyc")
    }
}