// MainActivity.kt
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
