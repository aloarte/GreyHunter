package com.devalr.createminiature

import android.content.Context
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.devalr.createminiature.components.AddMiniatureScreenContent
import com.devalr.createminiature.interactions.Action.AddMiniature
import com.devalr.createminiature.interactions.Action.ChangeImage
import com.devalr.createminiature.interactions.Action.ChangeName
import com.devalr.createminiature.interactions.Action.Load
import com.devalr.createminiature.interactions.Action.Return
import com.devalr.createminiature.interactions.ErrorType
import com.devalr.createminiature.interactions.ErrorType.AddDatabase
import com.devalr.createminiature.interactions.ErrorType.BadId
import com.devalr.createminiature.interactions.ErrorType.EditDatabase
import com.devalr.createminiature.interactions.ErrorType.EmptyName
import com.devalr.createminiature.interactions.ErrorType.ErrorUpdatingProgress
import com.devalr.createminiature.interactions.ErrorType.ImportImage
import com.devalr.createminiature.interactions.Event.LaunchSnackBarError
import com.devalr.createminiature.interactions.Event.NavigateBack
import com.devalr.framework.components.bottomsheet.ImagePickerHandler
import com.devalr.framework.components.empty.EmptyScreen
import com.devalr.framework.components.snackbar.SnackBarType
import com.devalr.framework.components.snackbar.SnackBarVisualsCustom
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun AddMiniatureScreen(
    viewModel: AddMiniatureViewModel = koinInject(),
    snackBarHostState: SnackbarHostState,
    projectId: Long,
    miniatureId: Long?,
    onBack: () -> Unit
) {
    var showImagePicker by remember { mutableStateOf(false) }
    val state = viewModel.uiState.collectAsState().value
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

    Scaffold { innerPadding ->
        if (state.error) {
            EmptyScreen { viewModel.onAction(Return) }
        } else {
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
}

private fun getSnackBarMessage(context: Context, error: ErrorType): String =
    when (error) {
        BadId -> context.getString(R.string.error_add_miniature_bad_id)
        EmptyName -> context.getString(R.string.error_add_miniature_empty_name)
        AddDatabase -> context.getString(R.string.error_add_miniature_add_database)
        EditDatabase -> context.getString(R.string.error_add_miniature_edit_database)
        ErrorUpdatingProgress -> context.getString(R.string.error_add_miniature_update_progress)
        ImportImage -> context.getString(R.string.error_add_miniature_import_images)
    }

