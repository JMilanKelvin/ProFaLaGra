package com.example.profalagra.features.lista

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.profalagra.databinding.GraphicItemListBinding

class GraphicsAdapter(
    private val originalItems: List<String>,
    private val onMoreClick: (String, View) -> Unit
) : RecyclerView.Adapter<GraphicsAdapter.GraphicItemHolder>() {

    private var filteredItems: MutableList<String> = originalItems.toMutableList()

    inner class GraphicItemHolder(val binding: GraphicItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphicItemHolder {
        val binding = GraphicItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GraphicItemHolder(binding)
    }

    override fun onBindViewHolder(holder: GraphicItemHolder, position: Int) {
        val item = filteredItems[position]
        holder.binding.itemTitle.text = item
        holder.binding.moreButton.setOnClickListener {
            onMoreClick(item, it)
        }
    }

    override fun getItemCount() = filteredItems.size

    fun filter(query: String) {
        filteredItems.clear()
        if (query.isEmpty()) {
            filteredItems.addAll(originalItems)
        } else {
            val lowerQuery = query.lowercase()
            filteredItems.addAll(originalItems.filter {
                it.lowercase().contains(lowerQuery)
            })
        }
        notifyDataSetChanged()
    }
}
