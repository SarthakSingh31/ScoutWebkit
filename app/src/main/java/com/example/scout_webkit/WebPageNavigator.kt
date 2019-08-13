package com.example.scout_webkit

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class WebPageNavigator : FrameLayout {
    private var parentFragmentActivity : AppCompatActivity
    private var startUrl : String

    var turboWebViewFragments : Stack<TurboWebViewFragment>

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
            parentFragmentActivity.supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_right, R.anim.exit_left).replace(id, turboWebViewFragments.peek()).commit()
            parentFragmentActivity.invalidateOptionsMenu()
        }
    }

    fun backPressHandler() : Boolean {
        if (turboWebViewFragments.size > 1) {
            parentFragmentActivity.runOnUiThread {
                turboWebViewFragments.pop()
                parentFragmentActivity.supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_left, R.anim.exit_right).replace(id ,turboWebViewFragments.peek())
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
            parentFragmentActivity.invalidateOptionsMenu()

            if (visibility == View.VISIBLE && turboWebViewFragments.size < 1) {
                turboWebViewFragments.push(TurboWebViewFragment(startUrl, this))
                parentFragmentActivity.supportFragmentManager.beginTransaction()
                    .add(id, turboWebViewFragments.peek())
                    .commit()
            }
        }
    }

    fun updateActionBar(menu: Menu) {
        parentFragmentActivity.title = turboWebViewFragments.peek().getTitle()
        if (turboWebViewFragments.size > 1)
            parentFragmentActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}