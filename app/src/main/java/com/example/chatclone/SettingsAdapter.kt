package com.example.chatclone

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SettingsAdapter(
    private val options: List<Pair<String, Int>>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_settings, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (title, iconRes) = options[position]
        holder.icon.setImageResource(iconRes)
        holder.title.text = title
        holder.itemView.setOnClickListener { onItemClick(title) }
    }

    override fun getItemCount() = options.size

    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.ivIcon)
        val title: TextView = itemView.findViewById(R.id.tvTitle)
    }
}