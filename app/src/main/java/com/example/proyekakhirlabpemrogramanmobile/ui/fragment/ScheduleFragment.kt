package com.example.proyekakhirlabpemrogramanmobile.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.proyekakhirlabpemrogramanmobile.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleFragment : Fragment() {

    private lateinit var textViewDate: TextView
    private lateinit var imageViewLeft: ImageView
    private lateinit var imageViewRight: ImageView
    private val calendar = Calendar.getInstance()
    private val locale = Locale("id", "ID")
    private val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", locale)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_schedule, container, false)

        textViewDate = view.findViewById(R.id.textViewDate)
        imageViewLeft = view.findViewById(R.id.imageViewLeft)
        imageViewRight = view.findViewById(R.id.imageViewRight)

        updateDateText()

        // Event untuk TextView (menampilkan DatePickerDialog)
        textViewDate.setOnClickListener {
            showDatePicker()
        }

        // Event untuk panah kiri (kurangi 1 hari)
        imageViewLeft.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            updateDateText()
        }

        // Event untuk panah kanan (tambah 1 hari)
        imageViewRight.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            updateDateText()
        }

        return view
    }

    private fun updateDateText() {
        textViewDate.text = dateFormat.format(calendar.time)
    }

    private fun showDatePicker() {
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                updateDateText()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}