package com.example.scout_webkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import android.app.Activity
import android.net.Uri
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class TurboWebViewFragment : Fragment {
    private var startUrl : String
    private var parentNavigator : WebPageNavigator?
    private var fragmentView: View? = null

    private lateinit var webView: WebView

    constructor(startUrl : String, parentNavigator : WebPageNavigator?) : super() {
        this.startUrl = startUrl
        this.parentNavigator = parentNavigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TurboWebViewFragment", "onCreateView: $container")
        if (fragmentView == null) {
            return inflater.inflate(R.layout.webview_layout, container, false)
        }
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (fragmentView == null) {
            webView = (view as ViewGroup).getChildAt(1) as WebView
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.loadUrl(startUrl)
            webView.addJavascriptInterface(JavaScriptBridge(), "TurboBridge")
            webView.webViewClient = TurboWebViewClient()
            webView.webChromeClient = CustomWebChromeClient()

            fragmentView = view
        }
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    fun getTitle(): String {
        return if (::webView.isInitialized)
            webView.title
        else
            ""
    }

    private inner class JavaScriptBridge {

        @JavascriptInterface
        fun visitProposedToLocationWithAction(location: String, action: String) {
            Log.d("JavaScriptBridge", "visitProposedToLocationWithAction: $location")
            parentNavigator?.addAnotherView(location)
        }
    }

    private inner class TurboWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            webView.evaluateJavascript(
                "Turbolinks.controller.adapter.visitProposedToLocationWithAction = (location, action) => { console.log(location.absoluteURL); TurboBridge.visitProposedToLocationWithAction(location.absoluteURL, action); return false; }",
                null
            )
            (fragmentView as SwipeRefreshLayout).isRefreshing = false
            activity?.title = webView.title
            super.onPageFinished(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            (context as Activity).startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            return true
        }
    }

    private inner class CustomWebChromeClient : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
            Log.d(
                "MyApplication", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId()
            )
            return super.onConsoleMessage(consoleMessage)
        }
    }
}