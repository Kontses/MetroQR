package com.traxis.metroqr

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.journeyapps.barcodescanner.BarcodeView
import com.traxis.metroqr.databinding.ActivityMainBinding
import com.traxis.metroqr.utils.SettingsManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        
        auth = Firebase.auth
        settingsManager = (application as MetroQRApplication).getSettingsManager()

        setupToolbar()
        setupBarcodeScanner()
        setupSearchView()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.menuButton.setOnClickListener {
            // TODO: Implement menu
        }
        binding.languageButton.setOnClickListener {
            // TODO: Implement language change
        }
        binding.themeButton.setOnClickListener {
            // TODO: Implement theme change
        }
    }

    private fun setupBarcodeScanner() {
        binding.barcodeScanner.decodeContinuous { result ->
            result.text?.let { barcode ->
                // TODO: Handle barcode result
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Handle search submission
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Handle text changes
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.barcodeScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeScanner.pause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_documentation -> {
                startActivity(Intent(this, DocumentationActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
} 