package com.traxis.metroqr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.traxis.metroqr.R

class DocumentationAdapter(private val items: List<DocumentationItem>) :
    RecyclerView.Adapter<DocumentationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleTextView)
        val description: TextView = view.findViewById(R.id.descriptionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_documentation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.description.text = item.description
    }

    override fun getItemCount() = items.size
}

data class DocumentationItem(
    val title: String,
    val description: String
) 