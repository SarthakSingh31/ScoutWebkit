package com.example.scout_webkit

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils



class MainActivity : AppCompatActivity() {
    private val CAMPUS_OPTIONS : Array<String> = arrayOf("Seattle", "Bothell", "Tacoma")
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
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showCampusChooser() {
        var defaultCampus = 0

        val sharedPreferences: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
        if (sharedPreferences.contains("selectedCampus"))
            defaultCampus = CAMPUS_OPTIONS.indexOf(sharedPreferences.getString("selectedCampus", null))

        val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Select a Campus")
        alertBuilder.setSingleChoiceItems(CAMPUS_OPTIONS, defaultCampus) { dialogInterface: DialogInterface, i: Int ->
            with (sharedPreferences.edit()) {
                if (sharedPreferences.contains("selectedCampus")) {
                    remove("selectedCampus")
                }
                putString("selectedCampus", CAMPUS_OPTIONS[i])
                apply()
            }

            webPageNavigators.forEach { it.hardReloadWebPage() }
            dialogInterface.dismiss()
        }

        alertBuilder.create().show()
    }
}
