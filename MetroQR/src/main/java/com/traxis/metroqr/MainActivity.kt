package com.traxis.metroqr

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.traxis.metroqr.databinding.ActivityMainBinding
import com.traxis.metroqr.databinding.NavHeaderMainBinding
import com.traxis.metroqr.databinding.UserMenuBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userMenuBinding: UserMenuBinding
    private lateinit var searchResultAdapter: SearchResultAdapter
    private lateinit var navHeaderBinding: NavHeaderMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        auth = Firebase.auth
        sharedPreferences = getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)

        // Setup navigation header
        navHeaderBinding = NavHeaderMainBinding.bind(navView.getHeaderView(0))
        updateUserInfo()

        // Setup user menu
        setupUserMenu()

        // Setup search functionality
        setupSearch()

        // Setup FAB for QR code scanning
        binding.fab.setOnClickListener {
            // Launch the QR code scanner activity
            startActivity(Intent(this, CustomScannerActivity::class.java))
        }
    }

    private fun setupUserMenu() {
        userMenuBinding = UserMenuBinding.inflate(layoutInflater)
        val userMenu = userMenuBinding.root

        // Set user info
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userMenuBinding.userEmail.text = currentUser.email
            userMenuBinding.companyLabel.text = getString(R.string.company_label)
        }

        // Setup language switch
        val isEnglish = sharedPreferences.getBoolean("is_english", false)
        userMenuBinding.languageSwitch.isChecked = isEnglish
        userMenuBinding.languageSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("is_english", isChecked).apply()
            recreate()
        }

        // Setup dark theme switch
        val isDarkTheme = sharedPreferences.getBoolean("dark_theme", false)
        userMenuBinding.darkThemeSwitch.isChecked = isDarkTheme
        userMenuBinding.darkThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("dark_theme", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // Add menu to navigation view
        binding.navView.addHeaderView(userMenu)
    }

    private fun setupSearch() {
        val searchView = binding.searchView
        val searchResultsRecyclerView = binding.searchResultsRecyclerView

        // Initialize adapter
        searchResultAdapter = SearchResultAdapter { searchResult ->
            // Handle search result click
            startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse(searchResult.url)))
            searchResultsRecyclerView.visibility = android.view.View.GONE
        }

        // Setup RecyclerView
        searchResultsRecyclerView.apply {
            adapter = searchResultAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    performSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    performSearch(newText)
                } else {
                    searchResultAdapter.updateResults(emptyList())
                    searchResultsRecyclerView.visibility = android.view.View.GONE
                }
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        // TODO: Implement actual search logic here
        // For now, we'll use dummy data
        val dummyResults = listOf(
            SearchResult("Documentation for $query", "https://docs.example.com/$query"),
            SearchResult("Tutorial: $query basics", "https://tutorial.example.com/$query"),
            SearchResult("$query API Reference", "https://api.example.com/$query")
        )
        searchResultAdapter.updateResults(dummyResults)
        binding.searchResultsRecyclerView.visibility = android.view.View.VISIBLE
    }

    private fun updateUserInfo() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navHeaderBinding.textView.text = currentUser.email
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Handle settings
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle home navigation
            }
            R.id.nav_gallery -> {
                // Handle gallery navigation
            }
            R.id.nav_slideshow -> {
                // Handle slideshow navigation
            }
            else -> {
                auth.signOut()
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}