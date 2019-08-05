package com.example.scout_webkit

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.util.*

class WebPageNavigator : FrameLayout {
    private var parentFragmentActivity : MainActivity
    private var turboWebViewFragments : Stack<TurboWebViewFragment>

    constructor(context : Context, attrs : AttributeSet) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.WebPageNavigator,
            0, 0
        )

        parentFragmentActivity = context as MainActivity

        var startUrl : String
        try {
            startUrl = a.getString(R.styleable.WebPageNavigator_startUrl)!!
        } finally {
            a.recycle()
        }

        turboWebViewFragments = Stack()
        turboWebViewFragments.push(TurboWebViewFragment(startUrl, this))
        parentFragmentActivity.supportFragmentManager.beginTransaction().add(id, turboWebViewFragments.peek()).commit()
    }

    fun addAnotherView (url : String) {
        turboWebViewFragments.push(TurboWebViewFragment(url, this))
        parentFragmentActivity.addAnotherView(id, turboWebViewFragments)
    }
}