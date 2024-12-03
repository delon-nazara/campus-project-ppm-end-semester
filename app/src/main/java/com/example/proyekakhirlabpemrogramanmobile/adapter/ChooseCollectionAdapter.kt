package com.example.proyekakhirlabpemrogramanmobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyekakhirlabpemrogramanmobile.databinding.ItemChooseCollectionBinding

class ChooseCollectionAdapter(
    private val imageUrls: List<String>,
    private val onImageClick: (String) -> Unit
) : RecyclerView.Adapter<ChooseCollectionAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemChooseCollectionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemChooseCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Glide.with(holder.itemView.context).load(imageUrl).into(holder.binding.imageView)

        holder.binding.imageView.setOnClickListener {
            onImageClick(imageUrl)
        }
    }

    override fun getItemCount() = imageUrls.size
}
