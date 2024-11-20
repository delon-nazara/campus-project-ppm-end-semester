// OutfitAdapter.kt
package com.example.proyekakhirlabpemrogramanmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

// Data model untuk outfit
data class Outfit(val imageResId: Int)

// Adapter untuk RecyclerView
class OutfitAdapter(private val outfitList: List<Outfit>) : RecyclerView.Adapter<OutfitAdapter.OutfitViewHolder>() {

    // ViewHolder untuk item outfit
    class OutfitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.outfitImage)
    }

    // Membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutfitViewHolder {
        // Inflate layout item_outfit.xml
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_outfit, parent, false)
        return OutfitViewHolder(view)
    }

    // Menghubungkan data ke ViewHolder
    override fun onBindViewHolder(holder: OutfitViewHolder, position: Int) {
        // Mengambil data dari daftar outfit berdasarkan posisi
        val outfit = outfitList[position]

        // Mengatur gambar dari resource id pada ImageView
        holder.imageView.setImageResource(outfit.imageResId)
    }

    // Mengembalikan jumlah item dalam daftar outfit
    override fun getItemCount(): Int {
        return outfitList.size
    }
}
