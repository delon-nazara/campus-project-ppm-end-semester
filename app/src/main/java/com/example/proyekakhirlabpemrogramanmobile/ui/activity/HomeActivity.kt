package com.example.proyekakhirlabpemrogramanmobile.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cloudinary.android.MediaManager
import com.example.proyekakhirlabpemrogramanmobile.ui.fragment.CollectionFragment
import com.example.proyekakhirlabpemrogramanmobile.ui.fragment.OutfitFragment
import com.example.proyekakhirlabpemrogramanmobile.ui.fragment.ProfileFragment
import com.example.proyekakhirlabpemrogramanmobile.R
import com.example.proyekakhirlabpemrogramanmobile.ui.fragment.ScheduleFragment
import com.example.proyekakhirlabpemrogramanmobile.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.github.cdimascio.dotenv.dotenv

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase auth initialization
        auth = Firebase.auth
        val user = auth.currentUser
        val userEmail = user?.email

        // Cloudinary initialization
        val dotenv = dotenv {
            directory = "/assets"
            filename = "env"
        }
        val config = hashMapOf(
            "cloud_name" to dotenv["CLOUD_NAME"],
            "secure" to true
        )
        MediaManager.init(this, config)

        // Binding setup
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set default fragment
        replaceFragment(CollectionFragment())

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