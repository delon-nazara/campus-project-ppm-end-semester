package com.example.proyekakhirlabpemrogramanmobile.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyekakhirlabpemrogramanmobile.R
import com.example.proyekakhirlabpemrogramanmobile.databinding.ActivityCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var imageCapture: ImageCapture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding setup
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.paddingBottom)
            insets
        }

        binding.closeIcon.setOnClickListener {
            finish()
        }

        if (isCameraPermissionGranted()) {
            startCamera()
        } else {
            requestCameraPermission()
        }

        binding.captureIcon.setOnClickListener {
            captureImage()
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
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
                finish()
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
                it.surfaceProvider = binding.cameraPreview.surfaceProvider
            }
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this,
                    getString(R.string.camera_failed_to_start),
                    Toast.LENGTH_SHORT
                ).show()
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
                    val intent = Intent(applicationContext, CameraEditActivity::class.java)
                    intent.putExtra("fullImagePath", photoFile.absolutePath)
                    startActivity(intent)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(applicationContext, getString(R.string.camera_failed_to_capture), Toast.LENGTH_SHORT)
                        .show()
                }

            }
        )
    }

}