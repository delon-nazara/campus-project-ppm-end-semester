package com.example.proyekakhirlabpemrogramanmobile

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.canhub.cropper.CropImageView
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.auth.FirebaseAuth
import io.github.cdimascio.dotenv.dotenv
import java.io.File
import java.io.FileOutputStream

class BackendEditScreenActivity : AppCompatActivity() {

    private lateinit var cropImageView: CropImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_backend_edit_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val oriImagePath = intent.getStringExtra("oriImagePath") ?: return
        val oriImageBitmap = BitmapFactory.decodeFile(oriImagePath)

        cropImageView = findViewById(R.id.cropImageView)
        cropImageView.setImageBitmap(oriImageBitmap)
        cropImageView.rotateImage(90)

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
            showSaveImageDialog(oriImagePath)
        }

    }

    private fun showSaveImageDialog(oriImagePath: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.modal_edit_picture)

        val previewImage = dialog.findViewById<ImageView>(R.id.previewImage)
        val croppedImageBitmap = cropImageView.getCroppedImage()
        previewImage.setImageBitmap(croppedImageBitmap)

        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        val imageNameInput = dialog.findViewById<EditText>(R.id.imageNameInput)
        val imageTypeSpinner = dialog.findViewById<Spinner>(R.id.imageTypeSpinner)

        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            if (imageNameInput.text.isEmpty()) {
                Toast.makeText(applicationContext, "Name cannot be empty!", Toast.LENGTH_SHORT).show()
            } else {
                val imageName = imageNameInput.text.toString()
                val imageType = imageTypeSpinner.selectedItem.toString()
                val fileName = "$imageType - $imageName.jpeg"
                val file = File(filesDir, fileName)
                FileOutputStream(file).use {
                    croppedImageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
                Toast.makeText(applicationContext, "Upload Image...", Toast.LENGTH_SHORT).show()
                uploadImageToCloudinary(file, fileName, oriImagePath)
            }
        }

        dialog.show()
    }

    private fun uploadImageToCloudinary(file: File, fileName: String, oriImagePath: String) {
        val dotenv = dotenv {
            directory = "/assets"
            filename = "env"
        }

        val config = hashMapOf(
            "cloud_name" to dotenv["CLOUD_NAME"],
            "secure" to true
        )
        MediaManager.init(this, config)

        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "unknown_user"
        val folderPath = "Project/$userEmail"

        MediaManager
            .get()
            .upload(file.absolutePath)
            .unsigned(dotenv["UPLOAD_PRESET_UNSIGNED"])
            .option("folder", folderPath)
            .option("public_id", fileName)
            .callback(object : UploadCallback {

                override fun onStart(requestId: String) {
                    Log.d("Cloudinary", "Upload started: $requestId")
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    val progress = (bytes.toDouble() / totalBytes) * 100
                    Log.d("Cloudinary", "Progress: $progress%")
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    Toast.makeText(applicationContext, "Upload Successful", Toast.LENGTH_SHORT).show()
                    Log.d("Cloudinary", "Upload successful: $resultData")
                    file.delete()
                    File(filesDir, oriImagePath).delete()
                    val intent = Intent(applicationContext, BackendCollectionScreenActivity ::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    Toast.makeText(applicationContext, "Upload failed. Try again!", Toast.LENGTH_SHORT).show()
                    Log.e("Cloudinary", "Upload error: $error")
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    Log.d("Cloudinary", "Upload rescheduled: $error")
                }
            })
            .dispatch()
    }

}