package com.example.proyekakhirlabpemrogramanmobile.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.proyekakhirlabpemrogramanmobile.R
import com.example.proyekakhirlabpemrogramanmobile.databinding.ActivityCameraEditBinding
import com.example.proyekakhirlabpemrogramanmobile.databinding.ModalEditPictureBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.github.cdimascio.dotenv.dotenv
import java.io.File
import java.io.FileOutputStream

class CameraEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraEditBinding
    private lateinit var dialogBinding: ModalEditPictureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.paddingBottom)
            insets
        }

        val fullImagePath = intent.getStringExtra("fullImagePath") ?: return
        val fullImageBitmap = BitmapFactory.decodeFile(fullImagePath)

        binding.cropImageView.setImageBitmap(fullImageBitmap)
        binding.cropImageView.rotateImage(90)

        binding.backButton.setOnClickListener {
            startCameraActivity()
        }

        binding.flipButton.setOnClickListener {
            binding.cropImageView.flipImageHorizontally()
        }

        binding.rotateLeftButton.setOnClickListener {
            binding.cropImageView.rotateImage(-90)
        }

        binding.rotateRightButton.setOnClickListener {
            binding.cropImageView.rotateImage(90)
        }

        binding.doneButton.setOnClickListener {
            showSaveImageDialog(fullImagePath)
        }
    }

    private fun startCameraActivity() {
        startActivity(Intent(this, CameraActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun showSaveImageDialog(fullImagePath: String) {
        dialogBinding = ModalEditPictureBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding.root)

        val croppedImageBitmap = binding.cropImageView.getCroppedImage()
        dialogBinding.previewImage.setImageBitmap(croppedImageBitmap)

        dialogBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.saveButton.setOnClickListener {
            val imageType = dialogBinding.imageTypeSpinner.selectedItem.toString().lowercase()
            val imageName = dialogBinding.imageNameInput.text.toString().lowercase()

            if (imageName.isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.image_name_empty), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, getString(R.string.upload_image), Toast.LENGTH_SHORT).show()

                val fileName = "$imageType - $imageName"
                val file = File(filesDir, fileName)

                FileOutputStream(file).use {
                    croppedImageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }

                uploadImageToCloudinary(file, fileName, fullImagePath, imageType)
            }
        }

        dialog.show()
    }

    private fun uploadImageToCloudinary(
        file: File,
        fileName: String,
        fullImagePath: String,
        imageType: String
    ) {
        val dotenv = dotenv {
            directory = "/assets"
            filename = "env"
        }

        val userEmail = Firebase.auth.currentUser?.email ?: "unknown_user"
        val folderPath = "My Wardrobe/$userEmail"

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
                    Log.d("Cloudinary", "Upload successful: $resultData")
                    Toast.makeText(applicationContext, getString(R.string.upload_image_successful), Toast.LENGTH_SHORT).show()

                    file.delete()
                    File(filesDir, fullImagePath).delete()

                    val db = Firebase.firestore
                    db.collection("collection")
                        .document(userEmail)
                        .collection(imageType)
                        .add(mapOf("url" to resultData["secure_url"].toString()))
                        .addOnSuccessListener {
                            Log.d("Firestore", "DocumentSnapshot successfully written!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error writing document", e)
                        }

                    startHomeActivity()
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    Toast.makeText(applicationContext, getString(R.string.upload_image_error), Toast.LENGTH_SHORT).show()
                    Log.e("Cloudinary", "Upload error: $error")
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    Log.d("Cloudinary", "Upload rescheduled: $error")
                }
            })
            .dispatch()
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java).apply {
            putExtra("defaultFragment", "CollectionFragment")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

}