package com.example.proyekakhirlabpemrogramanmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import io.github.cdimascio.dotenv.dotenv

class BackendCollectionScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_backend_collection_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val homeScreenButton = findViewById<Button>(R.id.buttonHomeScreen)
        homeScreenButton.setOnClickListener {
            val intent = Intent(this, BackendHomeScreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        val addCollectionButton = findViewById<Button>(R.id.buttonAddCollection)
        addCollectionButton.setOnClickListener {
            val popupMenu = PopupMenu(this, addCollectionButton)
            popupMenu.menuInflater.inflate(R.menu.add_collection_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.camera -> {
                        val intent = Intent(this, BackendCameraScreenActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        val dotenv = dotenv {
            directory = "/assets"
            filename = "env"
        }
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "unknown_user"

        val baseUrl = "https://res.cloudinary.com/${dotenv["CLOUD_NAME"]}/image/upload/My Wardrobe/${userEmail}/"
        val imageUrls = listOf(
            "${baseUrl}Shirt - Cat.jpeg.jpg",
            "${baseUrl}Shirt - Book.jpeg.jpg",
            "${baseUrl}Pants - Televisi.jpeg.jpg",
        )

        // data dummy
//        val tab1Images = listOf(R.drawable.top2, R.drawable.top3, R.drawable.top4, R.drawable.top, R.drawable.top4)
//        val tab2Images = listOf(R.drawable.top3, R.drawable.top, R.drawable.top4, R.drawable.top2, R.drawable.top3)
//        val tab3Images = listOf(R.drawable.top2, R.drawable.top3, R.drawable.top4, R.drawable.top, R.drawable.top4)
//        val tab4Images = listOf(R.drawable.top3, R.drawable.top, R.drawable.top4, R.drawable.top2, R.drawable.top3)

        // data dummy cloud
        val tab1Images = imageUrls.filter { it.contains("Shirt") }
        val tab2Images = imageUrls.filter { it.contains("Pants") }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.addItemDecoration(GridItemDecoration(3))

        fun updateRecyclerView(images: List<String>) {
            val adapter = GalleryAdapter(images)
            recyclerView.adapter = adapter
        }

        updateRecyclerView(tab1Images)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> updateRecyclerView(tab1Images)
                    1 -> updateRecyclerView(tab2Images)
                    2 -> updateRecyclerView(tab1Images)
                    3 -> updateRecyclerView(tab2Images)
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

    }
}

