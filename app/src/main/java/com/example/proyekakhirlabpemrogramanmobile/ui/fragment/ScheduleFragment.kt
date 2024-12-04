package com.example.proyekakhirlabpemrogramanmobile.ui.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekakhirlabpemrogramanmobile.R
import com.example.proyekakhirlabpemrogramanmobile.adapter.OutfitAdapter
import com.example.proyekakhirlabpemrogramanmobile.adapter.ScheduleAdapter
import com.example.proyekakhirlabpemrogramanmobile.databinding.FragmentOutfitBinding
import com.example.proyekakhirlabpemrogramanmobile.databinding.FragmentScheduleBinding
import com.example.proyekakhirlabpemrogramanmobile.ui.activity.CreateOutfitActivity
import com.example.proyekakhirlabpemrogramanmobile.ui.activity.CreateScheduleActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleFragment : Fragment() {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()
    private val locale = Locale("id", "ID")
    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", locale)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val view = binding.root

        updateDateText()
        loadSchedule()

        binding.textViewDate.setOnClickListener {
            showDatePicker()
        }

        binding.imageViewLeft.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            updateDateText()
            loadSchedule()
        }

        binding.imageViewRight.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            updateDateText()
            loadSchedule()
        }

        binding.addButton.setOnClickListener {
            startCreateScheduleActivity()
        }

        return view
    }

    private fun loadSchedule() {
        val schedule = mutableListOf<ScheduleAdapter.ScheduleItem>()

        val userEmail = Firebase.auth.currentUser?.email ?: "unknown_user"
        val db = Firebase.firestore
        val dbRef = db.collection("collection").document(userEmail)

        dbRef.collection("schedule").get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    if (it.data["scheduleDate"] == formatter.format(calendar.time)) {
                        schedule.add(
                            ScheduleAdapter.ScheduleItem(
                                it.data["scheduleName"].toString(),
                                it.data["scheduleOutfit"].toString(),
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

                if (schedule.isEmpty()) {
                    binding.emptyPlaceholder.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.emptyPlaceholder.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }

                binding.recyclerView.adapter = ScheduleAdapter(schedule)
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
            }
    }

    private fun updateDateText() {
        binding.textViewDate.text = dateFormat.format(calendar.time)
    }

    private fun showDatePicker() {
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                updateDateText()
                loadSchedule()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun startCreateScheduleActivity() {
        startActivity(Intent(requireContext(), CreateScheduleActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}