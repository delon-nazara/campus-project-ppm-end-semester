package com.example.proyekakhirlabpemrogramanmobile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.canhub.cropper.CropImageView

class BackendEditScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_backend_edit_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageUriString = intent.getStringExtra("image_uri")
        val imageUri = imageUriString?.let { Uri.parse(it) }

        val cropImageView = findViewById<CropImageView>(R.id.cropImageView)
        cropImageView.setImageUriAsync(imageUri)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, BackendCameraScreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        val flipButton = findViewById<ImageButton>(R.id.flipButton)
        flipButton.setOnClickListener {
            cropImageView.flipImageHorizontally()
        }

        val rotateLeftButton = findViewById<ImageButton>(R.id.rotateLeftButton)
        rotateLeftButton.setOnClickListener {
            cropImageView.rotateImage(-90)
        }

        val rotateRightButton = findViewById<ImageButton>(R.id.rotateRightButton)
        rotateRightButton.setOnClickListener {
            cropImageView.rotateImage(90)
        }

        val doneButton = findViewById<ImageButton>(R.id.doneButton)
        doneButton.setOnClickListener {
            // todo
        }

    }
}