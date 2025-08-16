package com.example.lungisa

import android.os.Bundle
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        // Set up the toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        // Set up the hamburger toggle
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Load default fragment (Home)
        replaceFragment(HomeFragment())
        navView.setCheckedItem(R.id.nav_home)

        // Handle menu clicks
        navView.setNavigationItemSelectedListener { menuItem ->

            // Only switch fragment if a different menu item is clicked
            if (!menuItem.isChecked) {
                when(menuItem.itemId) {
                    R.id.nav_home -> replaceFragment(HomeFragment())
                    R.id.nav_about -> replaceFragment(AboutFragment())
                    R.id.nav_services -> replaceFragment(ServicesFragment())
                    R.id.nav_news -> replaceFragment(NewsFragment())
                    R.id.nav_contact -> replaceFragment(ContactFragment())
                    R.id.nav_donations -> replaceFragment(DonationsFragment())
                }

                // Highlight the selected menu item
                menuItem.isChecked = true
            }

            // Close the drawer after selection
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    // Function to replace fragments
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // Handle back button to close drawer if open
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
