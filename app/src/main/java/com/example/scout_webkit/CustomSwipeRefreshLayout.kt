package com.example.scout_webkit

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class CustomSwipeRefreshLayout : SwipeRefreshLayout {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setOnRefreshListener {
            if (getChildAt(0) is WebView)
                (getChildAt(0) as WebView).reload()
        }
    }

    override fun canChildScrollUp(): Boolean {
        if (getChildAt(1) is WebView)
            return (getChildAt(1) as WebView).scrollY != 0
        return super.canChildScrollUp()
    }
}