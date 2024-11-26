package com.example.proyekakhirlabpemrogramanmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

        val shirtUrl = mutableListOf<String>()
        val pantsUrl = mutableListOf<String>()
        val accessoriesUrl = mutableListOf<String>()
        val shoesUrl = mutableListOf<String>()

        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "unknown_user"
        val db = FirebaseFirestore.getInstance()
        val dbRef = db.collection("users").document(userEmail)

        dbRef.collection("shirt").get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    shirtUrl.add(it.data["url"].toString())
                }
            }

        dbRef.collection("pants").get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    pantsUrl.add(it.data["url"].toString())
                }
            }

        dbRef.collection("accessories").get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    accessoriesUrl.add(it.data["url"].toString())
                }
            }

        dbRef.collection("shoes").get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    shoesUrl.add(it.data["url"].toString())
                }
            }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = GalleryAdapter(shirtUrl) // todo
        recyclerView.addItemDecoration(GridItemDecoration(3))

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> recyclerView.adapter = GalleryAdapter(shirtUrl)
                    1 -> recyclerView.adapter = GalleryAdapter(pantsUrl)
                    2 -> recyclerView.adapter = GalleryAdapter(shoesUrl)
                    3 -> recyclerView.adapter = GalleryAdapter(accessoriesUrl)
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })
    }
}

