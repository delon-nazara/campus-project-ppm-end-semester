package com.example.proyekakhirlabpemrogramanmobile.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekakhirlabpemrogramanmobile.R
import com.example.proyekakhirlabpemrogramanmobile.adapter.CollectionAdapter
import com.example.proyekakhirlabpemrogramanmobile.adapter.OutfitAdapter
import com.example.proyekakhirlabpemrogramanmobile.databinding.FragmentOutfitBinding
import com.example.proyekakhirlabpemrogramanmobile.ui.activity.CreateOutfitActivity
import com.example.proyekakhirlabpemrogramanmobile.ui.activity.HomeActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OutfitFragment : Fragment() {
    private var _binding: FragmentOutfitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOutfitBinding.inflate(inflater, container, false)
        val view = binding.root

        val outfitUrl = mutableListOf<OutfitAdapter.OutfitItem>()

        val userEmail = Firebase.auth.currentUser?.email ?: "unknown_user"
        val db = Firebase.firestore
        val dbRef = db.collection("collection").document(userEmail)

        dbRef.collection("outfit").get()
            .addOnSuccessListener { documents ->
                if (documents.documents.isEmpty()) {
                    binding.emptyPlaceholder.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.emptyPlaceholder.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }

                documents.forEach {
                    outfitUrl.add(
                        OutfitAdapter.OutfitItem(
                            it.data["outfitName"].toString(),
                            listOf(
                                it.data["topUrl"].toString(),
                                it.data["bottomUrl"].toString(),
                                it.data["shoesUrl"].toString(),
                                it.data["accessoriesUrl"].toString(),
                            )
                        )
                    )
                }

                binding.recyclerView.adapter = OutfitAdapter(outfitUrl)
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
            }

        binding.addButton.setOnClickListener {
            startCreateOutfitActivity()
        }

        return view
    }

    private fun startCreateOutfitActivity() {
        startActivity(Intent(requireContext(), CreateOutfitActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}