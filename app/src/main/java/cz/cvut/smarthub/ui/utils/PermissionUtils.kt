package cz.cvut.smarthub.ui.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class PermissionUtils(private val context: Context) {
    private val requestPermissionLauncher = (context as ComponentActivity).registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    fun requestCameraPermission(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                onPermissionGranted()
            }
            else -> {
                this.onPermissionGranted = onPermissionGranted
                this.onPermissionDenied = onPermissionDenied
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private lateinit var onPermissionGranted: () -> Unit
    private lateinit var onPermissionDenied: () -> Unit
}