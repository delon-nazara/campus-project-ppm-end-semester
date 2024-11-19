package com.example.proyekakhirlabpemrogramanmobile

import android.content.Intent
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayout

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

        // content tab (temporary)
        var selectedTabs = "Shirt"
        val text = mapOf(
            "Shirt" to "Baju",
            "Pants" to "Celana",
            "Shoes" to "Sepatu",
            "Accessories" to "Aksesoris"
        )

        val textView = findViewById<TextView>(R.id.textView)
        text.map { (key, value) ->
            if (key == selectedTabs) {
                textView.text = value
            }
        }

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedTabs = tab?.text.toString()

                text.map { (key, value) ->
                    if (key == selectedTabs) {
                        textView.text = value
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })

    }
}

