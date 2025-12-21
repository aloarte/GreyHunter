package com.devalr.framework.components.bottomsheet

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerHandler(
    show: Boolean,
    onImageChanged: (Uri) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    var showPermissionRationale by remember { mutableStateOf(false) }
    var permissionDeniedPermanently by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { onDismiss() }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { onImageChanged(it) }
            onDismiss()
        }
    )
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                tempCameraUri?.let { onImageChanged(it) }
            }
            onDismiss()
        }
    )
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                tempCameraUri = ComposeFileProvider.getImageUri(context)
                cameraLauncher.launch(tempCameraUri!!)
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        context.findActivity(),
                        Manifest.permission.CAMERA
                    )
                ) {
                    permissionDeniedPermanently = true
                } else {
                    showPermissionRationale = true
                }
            }
        }
    )

    fun handleCameraClick() {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                tempCameraUri = ComposeFileProvider.getImageUri(context)
                cameraLauncher.launch(tempCameraUri!!)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                context.findActivity(),
                Manifest.permission.CAMERA
            ) -> {
                showPermissionRationale = true
            }

            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    if (show) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
        ) {
            ImagePickerBottomSheetContent(
                onOpenCamera = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        handleCameraClick()
                    }
                },
                onOpenGallery = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        galleryLauncher.launch("image/*")
                    }
                }
            )
        }
    }

    val showPermissionSheet = showPermissionRationale || permissionDeniedPermanently
    if (showPermissionSheet) {
        val permissionSheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = {
                showPermissionRationale = false
                permissionDeniedPermanently = false
                onDismiss()
            },
            sheetState = permissionSheetState
        ) {
            PermissionBottomSheetContent(
                isPermanentlyDenied = permissionDeniedPermanently,
                onAccept = {
                    showPermissionRationale = false
                    permissionDeniedPermanently = false
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                },
                onGoToSettings = {
                    showPermissionRationale = false
                    permissionDeniedPermanently = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    settingsLauncher.launch(intent)
                },
                onCancel = {
                    showPermissionRationale = false
                    permissionDeniedPermanently = false
                    onDismiss()
                }
            )
        }
    }
}
