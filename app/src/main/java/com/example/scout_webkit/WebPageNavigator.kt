package com.example.scout_webkit

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import java.util.*

class WebPageNavigator : FrameLayout {
    private var parentFragmentActivity : MainActivity
    private var turboWebViewFragments : Stack<TurboWebViewFragment>
    private var startUrl : String

    constructor(context : Context, attrs : AttributeSet) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.WebPageNavigator,
            0, 0
        )
        try {
            startUrl = a.getString(R.styleable.WebPageNavigator_startUrl)!!
        } finally {
            a.recycle()
        }

        parentFragmentActivity = context as MainActivity
        turboWebViewFragments = Stack()
        // A TurboWebViewFragment gets added in onVisibilityChanged as the initial assignment of visibility triggers it
    }

    fun addAnotherView (url : String) {
        parentFragmentActivity.runOnUiThread {
            turboWebViewFragments.push(TurboWebViewFragment(url, this))
            parentFragmentActivity.supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_right, R.anim.exit_left).replace(id, turboWebViewFragments.peek()).addToBackStack(null).commit()
        }
    }

    fun backPressHandler() : Boolean {
        if (turboWebViewFragments.size > 1) {
            parentFragmentActivity.runOnUiThread {
                parentFragmentActivity.supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_left, R.anim.exit_right).remove(turboWebViewFragments.peek())
                    .commit()
                turboWebViewFragments.pop()
                parentFragmentActivity.supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_left, R.anim.exit_right).add(id ,turboWebViewFragments.peek())
                    .commit()
                parentFragmentActivity.title = turboWebViewFragments.peek().getTitle()
            }
            return true
        }
        return false
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (changedView is WebPageNavigator) {
            if (visibility == View.GONE) {
                if (turboWebViewFragments.size > 0) {
                    parentFragmentActivity.supportFragmentManager.beginTransaction()
                        .remove(turboWebViewFragments.peek())
                        .commit()
                }
                parentFragmentActivity.title = ""
            } else if (visibility == View.VISIBLE) {
                if (turboWebViewFragments.size > 0) {
                    parentFragmentActivity.supportFragmentManager.beginTransaction().add(
                        id,
                        turboWebViewFragments.peek()
                    ).commit()
                    parentFragmentActivity.title = turboWebViewFragments.peek().getTitle()
                }
                else {
                    turboWebViewFragments.push(TurboWebViewFragment(startUrl, this))
                    parentFragmentActivity.supportFragmentManager.beginTransaction()
                        .add(id, turboWebViewFragments.peek())
                        .commit()
                }
            }
        }
    }

    private fun resetActionBar() {
        parentFragmentActivity.title = ""
        parentFragmentActivity
    }

    private fun updateActionBar() {
        parentFragmentActivity.title = turboWebViewFragments.peek().getTitle()
    }
}