package com.example.proyekakhirlabpemrogramanmobile.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekakhirlabpemrogramanmobile.R
import com.example.proyekakhirlabpemrogramanmobile.adapter.ImageAdapter
import com.example.proyekakhirlabpemrogramanmobile.databinding.FragmentCollectionBinding

class CollectionFragment : Fragment() {

    private var _binding: FragmentCollectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerViewTop.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val images = listOf(
            R.drawable.top_1,
            R.drawable.top_2,
            R.drawable.top_3,
            R.drawable.top_4
        )
        binding.recyclerViewTop.adapter = ImageAdapter(images)

        binding.recyclerViewBottom.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val images2 = listOf(
            R.drawable.bottom_1,
            R.drawable.bottom_2,
            R.drawable.bottom_3,
            R.drawable.bottom_4
        )
        binding.recyclerViewBottom.adapter = ImageAdapter(images2)

        binding.recyclerViewAccessories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val images3 = listOf(
            R.drawable.accessories_1,
            R.drawable.accessories_2,
            R.drawable.accessories_3,
            R.drawable.accessories_4
        )
        binding.recyclerViewAccessories.adapter = ImageAdapter(images3)

        binding.recyclerViewShoes.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val images4 = listOf(
            R.drawable.shoes_1,
            R.drawable.shoes_2,
            R.drawable.shoes_3,
            R.drawable.shoes_4
        )
        binding.recyclerViewShoes.adapter = ImageAdapter(images4)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

