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
        parentFragmentActivity.runOnUiThread(Runnable {
            //var saveState = supportFragmentManager.saveFragmentInstanceState(supportFragmentManager.fragments[turboWebViewFragments.size - 2])
            parentFragmentActivity.supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_right, R.anim.exit_left).detach(turboWebViewFragments.peek()).addToBackStack(null).commit()
            turboWebViewFragments.push(TurboWebViewFragment(url, this))
            parentFragmentActivity.supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_right, R.anim.exit_left).add(id, turboWebViewFragments.peek()).addToBackStack(null).commit()
            //supportFragmentManager.fragments[turboWebViewFragments.size - 2].setInitialSavedState(saveState)
        })
    }

    fun backPressHandler() : Boolean {
        if (turboWebViewFragments.size > 1) {
            parentFragmentActivity.runOnUiThread(Runnable {
                parentFragmentActivity.supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_left, R.anim.exit_right).remove(turboWebViewFragments.peek())
                    .commit()
                turboWebViewFragments.pop()
                parentFragmentActivity.supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_left, R.anim.exit_right).attach(turboWebViewFragments.peek())
                    .commit()
            })
            return true
        }
        return false
    }
}