package com.example.scout_webkit

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.webkit.WebView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CustomSwipeRefreshLayout : SwipeRefreshLayout {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setOnRefreshListener {
            findViewById<WebView>(R.id.webView).reload()
        }
    }

    override fun onFinishInflate() {
        findViewById<FloatingActionButton>(R.id.filter_submit).hide()
        super.onFinishInflate()
    }

    override fun canChildScrollUp(): Boolean {
        return findViewById<WebView>(R.id.webView).scrollY != 0
    }
}