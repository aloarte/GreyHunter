package com.devalr.framework.components.dialog

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


@Composable
fun ImagePickerDialog(onImageChanged: (Uri) -> Unit, onCloseDialog: () -> Unit) {
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                onImageChanged(it)
                onCloseDialog()
            }
        }
    )
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                tempCameraUri?.let { onImageChanged(it) }
                onCloseDialog()
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                tempCameraUri = ComposeFileProvider.getImageUri(context)
                cameraLauncher.launch(tempCameraUri!!)
            } else {
                onCloseDialog()
                //TODO: HANDLE permission denied
            }
        }
    )

    AlertDialog(
        onDismissRequest = onCloseDialog,
        title = { Text("Seleccionar fuente de imagen") },
        text = { Text("¿Desde dónde quieres añadir la imagen?") },
        confirmButton = {
            TextButton(onClick = {
                galleryLauncher.launch("image/*")
            }) {
                Text("Galería")
            }
        },
        dismissButton = {
            TextButton(onClick = {

                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                        tempCameraUri = ComposeFileProvider.getImageUri(context)
                        cameraLauncher.launch(tempCameraUri!!)
                    }

                    else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text("Cámara")
            }
        }
    )
}
