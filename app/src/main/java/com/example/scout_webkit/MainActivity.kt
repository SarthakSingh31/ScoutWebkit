package com.example.scout_webkit

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils



class MainActivity : AppCompatActivity() {
    private lateinit var webPageNavigators: Array<WebPageNavigator>
    private var currWebViewIndex: Int = 0

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.discover_button -> {
                if (currWebViewIndex != 0) {
                    webPageNavigators[currWebViewIndex].visibility = View.GONE
                    currWebViewIndex = 0
                    webPageNavigators[currWebViewIndex].visibility = View.VISIBLE
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.food_button -> {
                if (currWebViewIndex != 1) {
                    webPageNavigators[currWebViewIndex].visibility = View.GONE
                    currWebViewIndex = 1
                    webPageNavigators[currWebViewIndex].visibility = View.VISIBLE
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.study_button -> {
                if (currWebViewIndex != 2) {
                    webPageNavigators[currWebViewIndex].visibility = View.GONE
                    currWebViewIndex = 2
                    webPageNavigators[currWebViewIndex].visibility = View.VISIBLE
                }
                return@OnNavigationItemSelectedListener true
            }

            R.id.tech_button -> {
                if (currWebViewIndex != 3) {
                    webPageNavigators[currWebViewIndex].visibility = View.GONE
                    currWebViewIndex = 3
                    webPageNavigators[currWebViewIndex].visibility = View.VISIBLE
                }
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
        webPageNavigators  = arrayOf(
            findViewById(R.id.discover_page),
            findViewById(R.id.food_page),
            findViewById(R.id.study_page),
            findViewById(R.id.tech_page)
        )
        title = ""
    }

    override fun onBackPressed() {
        if (!webPageNavigators[currWebViewIndex].backPressHandler())
            super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar, menu)
        super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            webPageNavigators[currWebViewIndex].updateActionBar(menu)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
