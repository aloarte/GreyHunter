package com.devalr.createproject

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.devalr.createproject.components.AddProjectScreenContent
import com.devalr.createproject.interactions.Action.AddProject
import com.devalr.createproject.interactions.Action.ChangeDescription
import com.devalr.createproject.interactions.Action.ChangeImage
import com.devalr.createproject.interactions.Action.ChangeName
import com.devalr.createproject.interactions.Action.Load
import com.devalr.createproject.interactions.Action.Return
import com.devalr.createproject.interactions.Event.NavigateBack
import com.devalr.framework.components.bottomsheet.ImagePickerHandler
import org.koin.compose.koinInject

@Composable
fun AddProjectScreen(
    viewModel: AddProjectViewModel = koinInject(),
    projectId: Long?,
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
    LaunchedEffect(true) { viewModel.onAction(Load(projectId)) }
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

    Scaffold { innerPadding ->
        AddProjectScreenContent(
            innerPadding = innerPadding,
            projectName = state.projectName,
            projectDescription = state.projectDescription,
            projectImage = state.projectImage,
            editMode = state.editMode,
            onPickImage = {
                showImagePicker = true
            },
            onChangeName = {
                viewModel.onAction(ChangeName(it))
            },
            onChangeDescription = {
                viewModel.onAction(ChangeDescription(it))
            },
            onAddProject = {
                viewModel.onAction(AddProject)
            },
            onNavigateBack = {
                viewModel.onAction(Return)
            }
        )
    }
}
