package com.example.proyekakhirlabpemrogramanmobile.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.proyekakhirlabpemrogramanmobile.R
import com.example.proyekakhirlabpemrogramanmobile.databinding.ActivityHomeBinding
import com.example.proyekakhirlabpemrogramanmobile.ui.fragment.CollectionFragment
import com.example.proyekakhirlabpemrogramanmobile.ui.fragment.OutfitFragment
import com.example.proyekakhirlabpemrogramanmobile.ui.fragment.ProfileFragment
import com.example.proyekakhirlabpemrogramanmobile.ui.fragment.ScheduleFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding setup
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add padding from status bar
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.paddingBottom)
            insets
        }

        // Set default fragment
        val defaultFragment = intent.getStringExtra("defaultFragment") ?: "CollectionFragment"
        when (defaultFragment) {
            "OutfitFragment" -> {
                replaceFragment(OutfitFragment())
                binding.bottomNavigation.selectedItemId = R.id.outfitMenu
            }

            "ScheduleFragment" -> {
                replaceFragment(ScheduleFragment())
                binding.bottomNavigation.selectedItemId = R.id.scheduleMenu
            }

            "ProfileFragment" -> {
                replaceFragment(ProfileFragment())
                binding.bottomNavigation.selectedItemId = R.id.profileMenu
            }

            else -> {
                replaceFragment(CollectionFragment())
                binding.bottomNavigation.selectedItemId = R.id.collectionMenu
            }
        }

        // Change fragment container based on selected menu
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.collectionMenu -> {
                    replaceFragment(CollectionFragment())
                    true
                }

                R.id.outfitMenu -> {
                    replaceFragment(OutfitFragment())
                    true
                }

                R.id.scheduleMenu -> {
                    replaceFragment(ScheduleFragment())
                    true
                }

                R.id.profileMenu -> {
                    replaceFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }

    }

    // Function for fragment replacement
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}