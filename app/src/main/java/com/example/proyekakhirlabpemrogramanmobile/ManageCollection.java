package com.example.clothingapp

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTop = findViewById<Button>(R.id.btnTop)
                val btnSkirt = findViewById<Button>(R.id.btnSkirt)
                val btnAccessories = findViewById<Button>(R.id.btnAccessories)
                val gridLayout = findViewById<GridLayout>(R.id.gridLayout)

                // Example of changing the grid based on button click
                btnTop.setOnClickListener {
            // Code to load Top items in gridLayout
        }

        btnSkirt.setOnClickListener {
            // Code to load Skirt items in gridLayout
        }

        btnAccessories.setOnClickListener {
            // Code to load Accessories items in gridLayout
        }
    }
}
