package com.traxis.metroqr

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.traxis.metroqr.adapters.DocumentationAdapter
import com.traxis.metroqr.adapters.DocumentationItem
import com.traxis.metroqr.databinding.ActivityDocumentationBinding

class DocumentationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDocumentationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.documentation)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val items = listOf(
            DocumentationItem(
                "Σάρωση QR",
                "Χρησιμοποιήστε την κάμερα της συσκευής σας για να σαρώσετε QR κώδικες σταθμών του μετρό."
            ),
            DocumentationItem(
                "Αναζήτηση",
                "Χρησιμοποιήστε τη λειτουργία αναζήτησης για να βρείτε γρήγορα σταθμούς."
            ),
            DocumentationItem(
                "Ρυθμίσεις",
                "Προσαρμόστε τη γλώσσα και το θέμα της εφαρμογής στις προτιμήσεις σας."
            )
        )

        binding.documentationRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DocumentationActivity)
            adapter = DocumentationAdapter(items)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onSupportNavigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
} 