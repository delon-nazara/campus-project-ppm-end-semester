package com.example.proyekakhirlabpemrogramanmobile.ui.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.proyekakhirlabpemrogramanmobile.R
import com.example.proyekakhirlabpemrogramanmobile.adapter.ChooseCollectionAdapter
import com.example.proyekakhirlabpemrogramanmobile.adapter.SpaceItemDecoration
import com.example.proyekakhirlabpemrogramanmobile.databinding.ActivityCreateOutfitBinding
import com.example.proyekakhirlabpemrogramanmobile.databinding.ModalChooseCollectionBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateOutfitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateOutfitBinding
    private var topUrlSelected = ""
    private var bottomUrlSelected = ""
    private var shoesUrlSelected = ""
    private var accessoriesUrlSelected = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateOutfitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.paddingBottom)
            insets
        }

        binding.backButton.setOnClickListener {
            finish()
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
                documents.forEach {
                    topUrl.add(it.data["url"].toString())
                }
            }

        dbRef.collection("bottom").get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    bottomUrl.add(it.data["url"].toString())
                }
            }

        dbRef.collection("shoes").get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    shoesUrl.add(it.data["url"].toString())
                }
            }

        dbRef.collection("accessories").get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    accessoriesUrl.add(it.data["url"].toString())
                }
            }

        binding.topImage.setOnClickListener {
            showImageDialog(topUrl, "top")
        }

        binding.bottomImage.setOnClickListener {
            showImageDialog(bottomUrl, "bottom")
        }

        binding.shoesImage.setOnClickListener {
            showImageDialog(shoesUrl, "shoes")
        }

        binding.accessoriesImage.setOnClickListener {
            showImageDialog(accessoriesUrl, "accessories")
        }

        binding.topRandom.setOnClickListener {
            randomImage(topUrl, binding.topImage)
        }

        binding.bottomRandom.setOnClickListener {
            randomImage(bottomUrl, binding.bottomImage)
        }

        binding.shoesRandom.setOnClickListener {
            randomImage(shoesUrl, binding.shoesImage)
        }

        binding.accessoriesRandom.setOnClickListener {
            randomImage(accessoriesUrl, binding.accessoriesImage)
        }

        binding.randomButton.setOnClickListener {
            randomImage(topUrl, binding.topImage)
            randomImage(bottomUrl, binding.bottomImage)
            randomImage(shoesUrl, binding.shoesImage)
            randomImage(accessoriesUrl, binding.accessoriesImage)
        }

        binding.saveButton.setOnClickListener {
            val nameOutfit = binding.nameInput.text.toString()
            if (nameOutfit.isEmpty()) {
                Toast.makeText(this, getString(R.string.outfit_name_empty), Toast.LENGTH_SHORT).show()
            } else if (topUrlSelected.isEmpty() && bottomUrlSelected.isEmpty() && shoesUrlSelected.isEmpty() && accessoriesUrlSelected.isEmpty()) {
                Toast.makeText(this, getString(R.string.outfit_image_empty), Toast.LENGTH_SHORT).show()
            } else {
                val newOutfit = mapOf(
                    "outfitName" to nameOutfit,
                    "topUrl" to topUrlSelected,
                    "bottomUrl" to bottomUrlSelected,
                    "shoesUrl" to shoesUrlSelected,
                    "accessoriesUrl" to accessoriesUrlSelected,
                )
                db.collection("collection")
                    .document(userEmail)
                    .collection("outfit")
                    .add(newOutfit)
                    .addOnSuccessListener {
                        Toast.makeText(this, getString(R.string.outfit_save_success), Toast.LENGTH_SHORT).show()
                        Log.d("Firestore", "DocumentSnapshot successfully written!")
                        startHomeActivity()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, getString(R.string.outfit_save_error), Toast.LENGTH_SHORT).show()
                        Log.w("Firestore", "Error writing document", e)
                    }
            }
        }
    }

    private fun randomImage(listUrl: List<String>, imageView: ImageView) {
        if (listUrl.isNotEmpty()) {
            val imageRandom = listUrl.random()

            Glide.with(this).load(imageRandom).into(imageView)
            when (imageView) {
                binding.topImage -> topUrlSelected = imageRandom
                binding.bottomImage -> bottomUrlSelected = imageRandom
                binding.shoesImage -> shoesUrlSelected = imageRandom
                binding.accessoriesImage -> accessoriesUrlSelected = imageRandom
            }
        }
    }

    private fun showImageDialog(imageUrls: List<String>, type: String) {
        val dialogBinding = ModalChooseCollectionBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding.root)

        if (imageUrls.isEmpty()) {
            dialogBinding.textView.visibility = View.VISIBLE
            dialogBinding.recyclerView.visibility = View.GONE
        } else {
            dialogBinding.textView.visibility = View.GONE
            dialogBinding.recyclerView.visibility = View.VISIBLE
        }

        val adapter = ChooseCollectionAdapter(imageUrls) { selectedImageUrl ->
            when (type) {
                "top" -> {
                    Glide.with(this).load(selectedImageUrl).into(binding.topImage)
                    topUrlSelected = selectedImageUrl
                }

                "bottom" -> {
                    Glide.with(this).load(selectedImageUrl).into(binding.bottomImage)
                    bottomUrlSelected = selectedImageUrl
                }

                "shoes" -> {
                    Glide.with(this).load(selectedImageUrl).into(binding.shoesImage)
                    shoesUrlSelected = selectedImageUrl
                }

                "accessories" -> {
                    Glide.with(this).load(selectedImageUrl).into(binding.accessoriesImage)
                    accessoriesUrlSelected = selectedImageUrl
                }
            }
            dialog.dismiss()
        }

        dialogBinding.recyclerView.adapter = adapter
        dialogBinding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        dialogBinding.recyclerView.addItemDecoration(SpaceItemDecoration(20))

        dialogBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java).apply {
            putExtra("defaultFragment", "OutfitFragment")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

}