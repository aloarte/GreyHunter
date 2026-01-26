package com.devalr.createminiature

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.devalr.createminiature.components.AddMiniatureScreenContent
import com.devalr.createminiature.interactions.Action.AddMiniature
import com.devalr.createminiature.interactions.Action.ChangeImage
import com.devalr.createminiature.interactions.Action.ChangeName
import com.devalr.createminiature.interactions.Action.Load
import com.devalr.createminiature.interactions.Action.Return
import com.devalr.createminiature.interactions.Event.NavigateBack
import com.devalr.framework.components.bottomsheet.ImagePickerHandler
import org.koin.compose.koinInject

@Composable
fun AddMiniatureScreen(
    viewModel: AddMiniatureViewModel = koinInject(),
    projectId: Long,
    miniatureId: Long?,
    onBack: () -> Unit
) {
    var showImagePicker by remember { mutableStateOf(false) }
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onBack()
            }
        }
    }
    LaunchedEffect(true) {
        viewModel.onAction(
            Load(
                projectId = projectId,
                miniatureId = miniatureId
            )
        )
    }
    if (showImagePicker) {
        ImagePickerHandler(
            show = true,
            onImageChanged = { uri ->
                viewModel.onAction(ChangeImage(uri))
                showImagePicker = false
            },
            onDismiss = {
                showImagePicker = false
            }
        )
    }

    Scaffold(
        topBar = { }
    ) { innerPadding ->
        AddMiniatureScreenContent(
            innerPadding = innerPadding,
            projectName = state.projectName,
            miniatureName = state.miniatureName,
            miniatureImage = state.miniatureImage,
            editMode = state.editMode,
            onNavigateBack = {
                viewModel.onAction(Return)
            },
            onPickImage = {
                showImagePicker = true
            },
            onChangeName = {
                viewModel.onAction(ChangeName(it))
            },
            onAddMiniature = {
                viewModel.onAction(AddMiniature)
            }
        )
    }
}
