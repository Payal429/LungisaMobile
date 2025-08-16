package com.example.lungisa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var topAppBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        topAppBar = findViewById(R.id.topAppBar)

        // Hamburger click opens drawer
        topAppBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Navigation item click listener
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment(), "Home")
                R.id.nav_about -> replaceFragment(AboutFragment(), "About")
                R.id.nav_services -> replaceFragment(ServicesFragment(), "Services")
                R.id.nav_news -> replaceFragment(NewsFragment(), "Latest News")
                R.id.nav_contact -> replaceFragment(ContactFragment(), "Contact")
                R.id.nav_donations -> replaceFragment(DonationsFragment(), "Donations")
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Show Home fragment by default
        replaceFragment(HomeFragment(), "Home")
        navView.setCheckedItem(R.id.nav_home)
    }

    private fun replaceFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        topAppBar.title = title
    }
}
