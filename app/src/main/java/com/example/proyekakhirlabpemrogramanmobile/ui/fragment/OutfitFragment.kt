package com.example.proyekakhirlabpemrogramanmobile.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekakhirlabpemrogramanmobile.R
import com.example.proyekakhirlabpemrogramanmobile.adapter.OutfitAdapter
import com.example.proyekakhirlabpemrogramanmobile.databinding.FragmentOutfitBinding

class OutfitFragment : Fragment() {
    private var _binding: FragmentOutfitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOutfitBinding.inflate(inflater, container, false)
        val view = binding.root

        val items = listOf(
            OutfitAdapter.OutfitItem(
                "Formal Outfit",
                listOf(R.drawable.top_1, R.drawable.bottom_1, R.drawable.shoes_1, R.drawable.accessories_1)
            ),
            OutfitAdapter.OutfitItem(
                "Riding Outfit",
                listOf(R.drawable.top_2, R.drawable.bottom_2, R.drawable.shoes_2, R.drawable.accessories_2)
            ),
            OutfitAdapter.OutfitItem(
                "Holiday Outfit",
                listOf(R.drawable.top_3, R.drawable.bottom_3, R.drawable.shoes_3, R.drawable.accessories_3)
            ),
            OutfitAdapter.OutfitItem(
                "Sports Outfit",
                listOf(R.drawable.top_4, R.drawable.bottom_4, R.drawable.shoes_4, R.drawable.accessories_4)
            )
        )
        binding.recyclerView.adapter = OutfitAdapter(items)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}