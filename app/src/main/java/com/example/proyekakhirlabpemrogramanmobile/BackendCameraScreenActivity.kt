package com.example.proyekakhirlabpemrogramanmobile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import io.github.cdimascio.dotenv.dotenv
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BackendCameraScreenActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_backend_camera_screen)
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

        previewView = findViewById(R.id.previewViewScreen)

        if (isCameraPermissionGranted()) {
            startCamera()
        } else {
            requestCameraPermission()
        }

        val imageCaptureButton = findViewById<Button>(R.id.buttonImageCapture)
        imageCaptureButton.setOnClickListener {
            captureImage()
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                explainAboutCameraPermission()
            }
        }.launch(Manifest.permission.CAMERA)
    }

    private fun explainAboutCameraPermission() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.camera_permission_required))
            .setMessage(getString(R.string.camera_permission_explained))
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(this, BackendHomeScreenActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            .create()
            .show()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(this,
                    getString(R.string.camera_failed_to_start, exc.message), Toast.LENGTH_SHORT).show()
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun getFileName(): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale("in", "ID"))
        return formatter.format(Date()) + ".jpg"
    }

    private fun captureImage() {
        val photoFile = File(filesDir, getFileName())
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(applicationContext, "Uploading image...", Toast.LENGTH_SHORT).show()
                    uploadImageToCloudinary(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(applicationContext, "Capture Image Failed", Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun uploadImageToCloudinary(file: File) {
        val dotenv = dotenv {
            directory = "/assets"
            filename = "env"
        }

        val config = hashMapOf(
            "cloud_name" to dotenv["CLOUD_NAME"],
            "secure" to true
        )
        MediaManager.init(this, config)

        MediaManager
            .get()
            .upload(file.absolutePath)
            .unsigned(dotenv["UPLOAD_PRESET_UNSIGNED"])
            .option("asset_folder", "temporary")
            .option("public_id", file.name)
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
                    if (file.exists()) {
                        file.delete()
                        Log.d("Cloudinary", "Local file deleted after upload")
                    }
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    Log.e("Cloudinary", "Upload error: $error")
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    Log.d("Cloudinary", "Upload rescheduled: $error")
                }
            })
            .dispatch()
    }

}