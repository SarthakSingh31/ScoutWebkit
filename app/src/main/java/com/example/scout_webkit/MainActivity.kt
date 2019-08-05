package com.example.scout_webkit

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import java.util.*

class MainActivity : FragmentActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    fun addAnotherView(id : Int, turboWebViewFragments : Stack<TurboWebViewFragment>) {
        runOnUiThread(Runnable {
            supportFragmentManager.beginTransaction().replace(id, turboWebViewFragments.peek()).commit()
        })
    }

    override fun onBackPressed() {
        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_right, R.anim.exit_left).replace(R.id.discover_page, TurboWebViewFragment("https://scout-test.s.uw.edu/h/seattle/food/", findViewById(R.id.discover_page))).commit()
        //super.onBackPressed()
    }
}
