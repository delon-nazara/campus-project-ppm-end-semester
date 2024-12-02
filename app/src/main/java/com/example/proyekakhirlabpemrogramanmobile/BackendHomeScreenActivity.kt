package com.example.proyekakhirlabpemrogramanmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.cloudinary.android.MediaManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import io.github.cdimascio.dotenv.dotenv

class BackendHomeScreenActivity : AppCompatActivity() {

//    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_backend_home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        auth = FirebaseAuth.getInstance()
//        val user = auth.currentUser
//        val userEmail = user?.email ?: "not found"

        val dotenv = dotenv {
            directory = "/assets"
            filename = "env"
        }

        val config = hashMapOf(
            "cloud_name" to dotenv["CLOUD_NAME"],
            "secure" to true
        )
        MediaManager.init(this, config)

//        val emailTextView = findViewById<TextView>(R.id.textViewEmail)
//        emailTextView.text = getString(R.string.email_user, userEmail)
//
//        val logoutButton = findViewById<Button>(R.id.buttonLogout)
//        logoutButton.setOnClickListener {
//            auth.signOut()
//            Toast.makeText(this, getString(R.string.logout_successful), Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, BackendMainScreenActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//        }

        replaceFragment(ScheduleFragment())

        val bottomNavigation = findViewById<NavigationBarView>(R.id.bottomNavigation)
        bottomNavigation.setOnItemSelectedListener { item ->
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

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}