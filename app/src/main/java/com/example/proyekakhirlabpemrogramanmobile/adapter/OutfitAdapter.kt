package com.example.proyekakhirlabpemrogramanmobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekakhirlabpemrogramanmobile.R

class OutfitAdapter(private val items: List<OutfitItem>) : RecyclerView.Adapter<OutfitAdapter.OutfitViewHolder>() {
    data class OutfitItem(
        val text: String,
        val images: List<Int>
    )

    inner class OutfitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardText: TextView = itemView.findViewById(R.id.cardText)
        val image1: ImageView = itemView.findViewById(R.id.image1)
        val image2: ImageView = itemView.findViewById(R.id.image2)
        val image3: ImageView = itemView.findViewById(R.id.image3)
        val image4: ImageView = itemView.findViewById(R.id.image4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutfitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_outfit, parent, false)
        return OutfitViewHolder(view)
    }

    override fun onBindViewHolder(holder: OutfitViewHolder, position: Int) {
        val item = items[position]
        holder.cardText.text = item.text
        holder.image1.setImageResource(item.images[0])
        holder.image2.setImageResource(item.images[1])
        holder.image3.setImageResource(item.images[2])
        holder.image4.setImageResource(item.images[3])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
