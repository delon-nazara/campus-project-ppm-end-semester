package com.example.proyekakhirlabpemrogramanmobile.ui.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.proyekakhirlabpemrogramanmobile.R
import com.example.proyekakhirlabpemrogramanmobile.adapter.ChooseCollectionAdapter
import com.example.proyekakhirlabpemrogramanmobile.adapter.ChooseOutfitAdapter
import com.example.proyekakhirlabpemrogramanmobile.adapter.OutfitAdapter
import com.example.proyekakhirlabpemrogramanmobile.adapter.SpaceItemDecoration
import com.example.proyekakhirlabpemrogramanmobile.databinding.ActivityCreateOutfitBinding
import com.example.proyekakhirlabpemrogramanmobile.databinding.ActivityCreateScheduleBinding
import com.example.proyekakhirlabpemrogramanmobile.databinding.ModalChooseCollectionBinding
import com.example.proyekakhirlabpemrogramanmobile.databinding.ModalChooseOutfitBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateScheduleBinding
    private var topUrlSelected = ""
    private var bottomUrlSelected = ""
    private var shoesUrlSelected = ""
    private var accessoriesUrlSelected = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.paddingBottom)
            insets
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        val calendar = Calendar.getInstance()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.dateInput.text = dateFormat.format(calendar.time)

        binding.dateInput.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                binding.dateInput.text = dateFormat.format(calendar.time)
            }, year, month, day)

            datePicker.show()
        }

        val outfitData = mutableListOf<OutfitAdapter.OutfitItem>()
        val outfitName = mutableListOf<String>()

        val userEmail = Firebase.auth.currentUser?.email ?: "unknown_user"
        val db = Firebase.firestore
        val dbRef = db.collection("collection").document(userEmail)

        dbRef.collection("outfit").get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    outfitName.add(it.data["outfitName"].toString())
                    outfitData.add(
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
            }

        binding.outfitInput.setOnClickListener {
            showImageDialog(outfitData)
        }

        binding.saveButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val date = binding.dateInput.text.toString()
            val outfit = binding.outfitInput.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(this, getString(R.string.schedule_name_empty), Toast.LENGTH_SHORT).show()
            } else if (outfit == getString(R.string.schedule_outfit)) {
                Toast.makeText(this, getString(R.string.schedule_outfit_empty), Toast.LENGTH_SHORT).show()
            } else {
                val newSchedule = mapOf(
                    "scheduleName" to name,
                    "scheduleDate" to date,
                    "scheduleOutfit" to outfit,
                    "topUrl" to topUrlSelected,
                    "bottomUrl" to bottomUrlSelected,
                    "shoesUrl" to shoesUrlSelected,
                    "accessoriesUrl" to accessoriesUrlSelected,
                )
                db.collection("collection")
                    .document(userEmail)
                    .collection("schedule")
                    .add(newSchedule)
                    .addOnSuccessListener {
                        Toast.makeText(this, getString(R.string.schedule_save_success), Toast.LENGTH_SHORT).show()
                        Log.d("Firestore", "DocumentSnapshot successfully written!")
                        startHomeActivity()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, getString(R.string.schedule_save_error), Toast.LENGTH_SHORT).show()
                        Log.w("Firestore", "Error writing document", e)
                    }
            }
        }
    }

    private fun showImageDialog(outfitData: List<OutfitAdapter.OutfitItem>) {
        val dialogBinding = ModalChooseOutfitBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding.root)

        val outfitName = mutableListOf<String>()
        outfitData.forEach {
            outfitName.add(it.name)
        }

        if (outfitName.isEmpty()) {
            dialogBinding.textView.visibility = View.VISIBLE
            dialogBinding.recyclerView.visibility = View.GONE
        } else {
            dialogBinding.textView.visibility = View.GONE
            dialogBinding.recyclerView.visibility = View.VISIBLE
        }

        val adapter = ChooseOutfitAdapter(outfitName) { selectedOutfit ->
            dialog.dismiss()
            binding.outfitInput.text = selectedOutfit

            outfitData.forEach {
                if (it.name == selectedOutfit) {
                    topUrlSelected = it.images[0]
                    bottomUrlSelected = it.images[1]
                    shoesUrlSelected = it.images[2]
                    accessoriesUrlSelected = it.images[3]

                    if (topUrlSelected.isNotEmpty()) {
                        binding.topImage.visibility = View.VISIBLE
                        Glide.with(this).load(topUrlSelected).into(binding.topImage)
                    } else {
                        binding.topImage.visibility = View.GONE
                    }

                    if (bottomUrlSelected.isNotEmpty()) {
                        binding.bottomImage.visibility = View.VISIBLE
                        Glide.with(this).load(bottomUrlSelected).into(binding.bottomImage)
                    } else {
                        binding.bottomImage.visibility = View.GONE
                    }

                    if (shoesUrlSelected.isNotEmpty()) {
                        binding.shoesImage.visibility = View.VISIBLE
                        Glide.with(this).load(shoesUrlSelected).into(binding.shoesImage)
                    } else {
                        binding.shoesImage.visibility = View.GONE
                    }

                    if (accessoriesUrlSelected.isNotEmpty()) {
                        binding.accessoriesImage.visibility = View.VISIBLE
                        Glide.with(this).load(accessoriesUrlSelected).into(binding.accessoriesImage)
                    } else {
                        binding.accessoriesImage.visibility = View.GONE
                    }
                }
            }
        }

        dialogBinding.recyclerView.adapter = adapter
        dialogBinding.recyclerView.layoutManager = LinearLayoutManager(this)

        dialogBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java).apply {
            putExtra("defaultFragment", "ScheduleFragment")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

}