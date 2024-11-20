package com.example.proyekakhirlabpemrogramanmobile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ElizabethCollectionScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_elizabeth_collection_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val fabAddOutfit = findViewById<FloatingActionButton>(R.id.fabAddOutfit)

        // Sample data outfit images
        val outfits = listOf(
            Outfit(R.drawable.outfit1),
            Outfit(R.drawable.outfit2),
            Outfit(R.drawable.outfit3)
            // Tambahkan gambar lain di sini
        )

        val adapter = OutfitAdapter(outfits)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 4)

        fabAddOutfit.setOnClickListener {
            // Logika untuk menambahkan outfit baru
        }
    }
}