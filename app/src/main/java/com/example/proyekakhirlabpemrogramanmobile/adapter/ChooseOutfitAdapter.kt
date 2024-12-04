package com.example.proyekakhirlabpemrogramanmobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekakhirlabpemrogramanmobile.databinding.ItemChooseOutfitBinding

class ChooseOutfitAdapter(
    private val items: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ChooseOutfitAdapter.TextViewHolder>() {

    inner class TextViewHolder(val binding: ItemChooseOutfitBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val binding = ItemChooseOutfitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TextViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.binding.textView.text = items[position]

        holder.binding.textView.setOnClickListener {
            onClick(holder.binding.textView.text.toString())
        }
    }

    override fun getItemCount() = items.size
}