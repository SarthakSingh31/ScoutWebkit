package com.example.scout_webkit

import android.content.Context
import android.util.AttributeSet
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class WebPageNavigator : FrameLayout {
    private var parentFragmentActivity : AppCompatActivity
    private var subUrl : String

    private var turboWebViewFragments : Stack<TurboWebViewFragment>

    constructor(context : Context, attrs : AttributeSet) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.WebPageNavigator,
            0, 0
        )
        try {
            subUrl = a.getString(R.styleable.WebPageNavigator_subUrl)!!
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
                parentFragmentActivity.invalidateOptionsMenu()
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
                turboWebViewFragments.push(TurboWebViewFragment(getCampusURL() + subUrl, this))
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
        else
            parentFragmentActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    fun hardReloadWebPage() {
        val initialVisibility = visibility
        visibility = View.GONE
        turboWebViewFragments.clear()
        visibility = initialVisibility
    }

    private fun getCampusURL(): String {
        val campusOptions : Array<String> = arrayOf("Seattle", "Bothell", "Tacoma")
        val campus: String

        val sharedPref = parentFragmentActivity.getPreferences(Context.MODE_PRIVATE)
        campus = if (!sharedPref.contains("campus")) {
            with(sharedPref.edit()) {
                putInt("campus", 0)
                apply()
            }
            campusOptions[0]
        } else {
            campusOptions[sharedPref.getInt("campus", -1)]
        }

        return "https://scout-test.s.uw.edu/h/" + campus.toLowerCase() + "/"
    }
}