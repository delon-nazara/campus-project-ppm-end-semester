package com.example.proyekakhirlabpemrogramanmobile

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi firebase firestore
        val db = Firebase.firestore

        // Membuat data dummy
        val user = hashMapOf(
            "name" to "Delon",
            "nim" to "221401073"
        )

        // Menambahkan data ke dalam firestore
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("firestore_log", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("firestore_log", "Error adding document", e)
            }
    }
}