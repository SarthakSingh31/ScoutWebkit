package com.example.scout_webkit

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment

class TurboWebViewFragment : Fragment {
    private var startUrl : String
    private var parentNavigator : WebPageNavigator?

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
        Log.d("TurboWebViewFragment", "onCreateView: $savedInstanceState")
        return inflater.inflate(R.layout.webview_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = view as WebView
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.loadUrl(startUrl)
        webView.addJavascriptInterface(JavaScriptBridge(), "TurboBridge")
        webView.webViewClient = TurboWebViewClient()
        webView.webChromeClient = CustomWebChromeClient()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    private inner class JavaScriptBridge {
        @JavascriptInterface
        fun visitProposedToLocationWithAction(location: String, action: String) {
            Log.d("JavaScriptBridge", "visitProposedToLocationWithAction: $location")
            parentNavigator?.addAnotherView(location)
        }
    }

    private inner class TurboWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            webView.evaluateJavascript(
                "Turbolinks.controller.adapter.__proto__.visitProposedToLocationWithAction = (location, action) => { console.log(location.absoluteURL); TurboBridge.visitProposedToLocationWithAction(location.absoluteURL, action); return false; }",
                null
            )
            Handler().postDelayed({ /*parentNavigator?.isRefreshing = false*/ }, 1500)
            super.onPageFinished(view, url)
            Log.d("TurboWebViewClient", "onPageFinished: " + webView.height + " " + webView.width)
        }

        /*private void openPageInNewTab(String url) {
            WebView currentWebView = webViewStack.peek();
            currentWebView.animate().translationX(currentWebView.getWidth()).setDuration(5000).setListener(null);
            Log.d("ScoutWebViewClient", "openPageInNewTab: " + currentWebView.getWidth());
        }*/
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