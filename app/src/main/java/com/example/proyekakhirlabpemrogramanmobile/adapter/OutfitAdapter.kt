package com.example.proyekakhirlabpemrogramanmobile.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyekakhirlabpemrogramanmobile.R

class OutfitAdapter(private val items: List<OutfitItem>) : RecyclerView.Adapter<OutfitAdapter.OutfitViewHolder>() {
    data class OutfitItem(
        val name: String,
        val images: List<String>
    )

    inner class OutfitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val outfitName: TextView = itemView.findViewById(R.id.outfitName)
        val topImage: ImageView = itemView.findViewById(R.id.topImage)
        val bottomImage: ImageView = itemView.findViewById(R.id.bottomImage)
        val shoesImage: ImageView = itemView.findViewById(R.id.shoesImage)
        val accessoriesImage: ImageView = itemView.findViewById(R.id.accessoriesImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutfitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_outfit, parent, false)
        return OutfitViewHolder(view)
    }

    override fun onBindViewHolder(holder: OutfitViewHolder, position: Int) {
        val item = items[position]
        holder.outfitName.text = item.name

        if (item.images[0].isNotEmpty()) {
            holder.topImage.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(item.images[0])
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.topImage)
        } else {
            holder.topImage.visibility = View.GONE
        }

        if (item.images[1].isNotEmpty()) {
            holder.bottomImage.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(item.images[1])
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.bottomImage)
        } else {
            holder.bottomImage.visibility = View.GONE
        }

        if (item.images[2].isNotEmpty()) {
            holder.shoesImage.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(item.images[2])
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.shoesImage)
        } else {
            holder.shoesImage.visibility = View.GONE
        }

        if (item.images[3].isNotEmpty()) {
            holder.accessoriesImage.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(item.images[3])
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.accessoriesImage)
        } else {
            holder.accessoriesImage.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}