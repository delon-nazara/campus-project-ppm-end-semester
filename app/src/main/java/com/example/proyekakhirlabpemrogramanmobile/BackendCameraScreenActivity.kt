package com.example.proyekakhirlabpemrogramanmobile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

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
            val intent = Intent(this, BackendHomeScreenActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
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
            if (isCameraPermissionGranted()) {
                captureImage()
            } else {
                requestCameraPermission()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_CAMERA_PERMISSION = 10
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, getString(R.string.camera_permission), Toast.LENGTH_SHORT).show()
            }
        }
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
                    getString(R.string.camera_failed, exc.message), Toast.LENGTH_SHORT).show()
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureImage() {
        val photoFile = File(getExternalFilesDir(null), "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(baseContext,
                        getString(R.string.image_capture_failed, exc.message), Toast.LENGTH_SHORT).show()
                    exc.printStackTrace()
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Toast.makeText(baseContext,
                        getString(R.string.image_capture_successful, photoFile.absolutePath), Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

}