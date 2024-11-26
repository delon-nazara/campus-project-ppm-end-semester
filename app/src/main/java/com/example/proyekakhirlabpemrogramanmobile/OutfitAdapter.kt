// OutfitAdapter.kt
package com.example.proyekakhirlabpemrogramanmobile

import android.os.Build.VERSION_CODES.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekakhirlabpemrogramanmobile.R


// Adapter untuk RecyclerView
class OutfitAdapter(private val outfitList: List<Outfit>) :
    RecyclerView.Adapter<OutfitAdapter.OutfitViewHolder>() {

    class OutfitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.outfitImage)
        val textView: TextView = itemView.findViewById(R.id.outfitName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutfitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_outfit_adapter, parent, false)
        return OutfitViewHolder(view)
    }

    override fun onBindViewHolder(holder: OutfitViewHolder, position: Int) {
        val outfit = outfitList[position]
        holder.textView.text = outfit.name  // Jika menggunakan nama properti outfitName
        holder.imageView.setImageResource(outfit.imageResId)
    }

    override fun getItemCount(): Int {
        return outfitList.size
    }
}

