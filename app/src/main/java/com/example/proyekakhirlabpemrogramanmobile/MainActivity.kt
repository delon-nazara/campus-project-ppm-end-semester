package com.example.proyekakhirlabpemrogramanmobile // Pastikan MainActivity ada di paket ini

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekakhirlabpemrogramanmobile.OutfitAdapter
import com.example.proyekakhirlabpemrogramanmobile.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // Deklarasi lateinit untuk daftar outfits
    private lateinit var outfits: List<Outfit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi RecyclerView dan FloatingActionButton
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val fabAddOutfit = findViewById<FloatingActionButton>(R.id.fabAddOutfit)

        // Memuat data outfits dari metode loadOutfitsFromDatabase()
        outfits = loadOutfitsFromDatabase()

        // Adapter dan layout manager
        val adapter = OutfitAdapter(outfits)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 4) // Grid dengan 4 kolom

        // Listener untuk tombol tambah outfit
        fabAddOutfit.setOnClickListener {
            // Logika untuk menambahkan outfit baru
            // Misalnya, buka dialog atau activity untuk memilih gambar
        }
    }

    // Metode untuk memuat data dari database (contoh, bisa disesuaikan dengan implementasi Anda)
    private fun loadOutfitsFromDatabase(): List<Outfit> {
        return listOf(
            Outfit("Outfit 1", "Deskripsi 1", R.drawable.top), // Gambar dari drawable
            Outfit("Outfit 2", "Deskripsi 2", R.drawable.top2),
            Outfit("Outfit 3", "Deskripsi 3", R.drawable.top3)
        )
    }
}

// Contoh model data Outfit
data class Outfit(
    val name: String,
    val description: String,
    val imageResId: Int // ID resource untuk gambar (drawable)
)
