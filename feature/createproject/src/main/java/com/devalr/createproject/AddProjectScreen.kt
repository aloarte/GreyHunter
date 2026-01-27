package com.devalr.createproject

import android.content.Context
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.devalr.createproject.components.AddProjectScreenContent
import com.devalr.createproject.interactions.Action.AddProject
import com.devalr.createproject.interactions.Action.ChangeDescription
import com.devalr.createproject.interactions.Action.ChangeImage
import com.devalr.createproject.interactions.Action.ChangeName
import com.devalr.createproject.interactions.Action.Load
import com.devalr.createproject.interactions.Action.Return
import com.devalr.createproject.interactions.ErrorType
import com.devalr.createproject.interactions.Event.LaunchSnackBarError
import com.devalr.createproject.interactions.Event.NavigateBack
import com.devalr.framework.components.bottomsheet.ImagePickerHandler
import com.devalr.framework.components.snackbar.GHSnackBar
import com.devalr.framework.components.snackbar.SnackBarType
import com.devalr.framework.components.snackbar.SnackBarVisualsCustom
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun AddProjectScreen(
    viewModel: AddProjectViewModel = koinInject(),
    projectId: Long?,
    onBack: () -> Unit
) {
    var showImagePicker by remember { mutableStateOf(false) }
    val state = viewModel.uiState.collectAsState().value
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onBack()
                is LaunchSnackBarError -> {
                    launch {
                        snackBarHostState.showSnackbar(
                            SnackBarVisualsCustom(
                                duration = SnackbarDuration.Short,
                                message = getSnackBarMessage(context, event.error),
                                type = SnackBarType.ERROR
                            )
                        )
                    }
                }
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
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                GHSnackBar(snackBarData = data)
            }
        }
    ) { innerPadding ->
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

private fun getSnackBarMessage(context: Context, error: ErrorType): String =
    when (error) {
        ErrorType.BadId -> context.getString(R.string.error_add_project_bad_id)
        ErrorType.EmptyTitle -> context.getString(R.string.error_add_project_empty_name)
        ErrorType.AddDatabase -> context.getString(R.string.error_add_project_add_database)
        ErrorType.EditDatabase -> context.getString(R.string.error_add_project_edit_database)
    }
