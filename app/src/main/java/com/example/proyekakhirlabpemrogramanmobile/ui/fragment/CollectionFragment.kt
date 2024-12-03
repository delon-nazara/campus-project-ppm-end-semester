package com.example.proyekakhirlabpemrogramanmobile.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekakhirlabpemrogramanmobile.adapter.CollectionAdapter
import com.example.proyekakhirlabpemrogramanmobile.databinding.FragmentCollectionBinding
import com.example.proyekakhirlabpemrogramanmobile.ui.activity.CameraActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CollectionFragment : Fragment() {
    private var _binding: FragmentCollectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.addCollection.setOnClickListener {
            startCameraActivity()
        }

        val topUrl = mutableListOf<String>()
        val bottomUrl = mutableListOf<String>()
        val shoesUrl = mutableListOf<String>()
        val accessoriesUrl = mutableListOf<String>()

        val userEmail = Firebase.auth.currentUser?.email ?: "unknown_user"
        val db = Firebase.firestore
        val dbRef = db.collection("collection").document(userEmail)

        dbRef.collection("top").get()
            .addOnSuccessListener { documents ->
                if (documents.documents.isEmpty()) {
                    binding.topEmptyPlaceholder.visibility = View.VISIBLE
                    binding.recyclerViewTop.visibility = View.GONE
                } else {
                    binding.topEmptyPlaceholder.visibility = View.GONE
                    binding.recyclerViewTop.visibility = View.VISIBLE
                }

                documents.forEach {
                    topUrl.add(it.data["url"].toString())
                }

                binding.recyclerViewTop.adapter = CollectionAdapter(topUrl)
                binding.recyclerViewTop.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

        dbRef.collection("bottom").get()
            .addOnSuccessListener { documents ->
                if (documents.documents.isEmpty()) {
                    binding.bottomEmptyPlaceholder.visibility = View.VISIBLE
                    binding.recyclerViewBottom.visibility = View.GONE
                } else {
                    binding.bottomEmptyPlaceholder.visibility = View.GONE
                    binding.recyclerViewBottom.visibility = View.VISIBLE
                }

                documents.forEach {
                    bottomUrl.add(it.data["url"].toString())
                }

                binding.recyclerViewBottom.adapter = CollectionAdapter(bottomUrl)
                binding.recyclerViewBottom.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

        dbRef.collection("shoes").get()
            .addOnSuccessListener { documents ->
                if (documents.documents.isEmpty()) {
                    binding.shoesEmptyPlaceholder.visibility = View.VISIBLE
                    binding.recyclerViewShoes.visibility = View.GONE
                } else {
                    binding.shoesEmptyPlaceholder.visibility = View.GONE
                    binding.recyclerViewShoes.visibility = View.VISIBLE
                }

                documents.forEach {
                    shoesUrl.add(it.data["url"].toString())
                }

                binding.recyclerViewShoes.adapter = CollectionAdapter(shoesUrl)
                binding.recyclerViewShoes.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

        dbRef.collection("accessories").get()
            .addOnSuccessListener { documents ->
                if (documents.documents.isEmpty()) {
                    binding.accessoriesEmptyPlaceholder.visibility = View.VISIBLE
                    binding.recyclerViewAccessories.visibility = View.GONE
                } else {
                    binding.accessoriesEmptyPlaceholder.visibility = View.GONE
                    binding.recyclerViewAccessories.visibility = View.VISIBLE
                }

                documents.forEach {
                    accessoriesUrl.add(it.data["url"].toString())
                }

                binding.recyclerViewAccessories.adapter = CollectionAdapter(accessoriesUrl)
                binding.recyclerViewAccessories.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

        return view
    }

    private fun startCameraActivity() {
        startActivity(Intent(requireContext(), CameraActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

