package com.devalr.createproject

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devalr.createproject.interactions.Action.AddProject
import com.devalr.createproject.interactions.Action.Load
import com.devalr.createproject.interactions.Action.ChangeDescription
import com.devalr.createproject.interactions.Action.ChangeImage
import com.devalr.createproject.interactions.Action.ChangeName
import com.devalr.createproject.interactions.Event.NavigateBack
import com.devalr.framework.components.button.GHButton
import com.devalr.framework.components.gh.GHImage
import com.devalr.framework.components.add.AddItemDescription
import com.devalr.framework.components.add.AddItemName
import com.devalr.framework.components.bottomsheet.ImagePickerHandler
import org.koin.compose.koinInject

@Composable
fun AddProjectScreen(
    viewModel: AddProjectViewModel = koinInject(),
    projectId:Long?,
    onBack: () -> Unit) {
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

    Scaffold(
        topBar = {
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(all = 10.dp)
        ) {
            GHImage(imageUri = state.projectImage, size = 100.dp) { showImagePicker = true }
            AddItemName(
                name = state.projectName,
                label = stringResource(R.string.label_project_name)
            ) {
                viewModel.onAction(ChangeName(it))
            }
            AddItemDescription(
                description = state.projectDescription,
                label = stringResource(R.string.label_project_description)
            ) {
                viewModel.onAction(ChangeDescription(it))
            }
            GHButton(text = if(state.editMode) stringResource(R.string.button_edit_project) else stringResource(R.string.button_add_project)) {
                viewModel.onAction(AddProject)
            }
        }
    }
}
