package com.devalr.projectdetail

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.res.stringResource
import com.devalr.framework.components.anim.LoadingIndicator
import com.devalr.framework.components.bottomsheet.ConfirmBottomSheetContent
import com.devalr.framework.components.empty.EmptyScreen
import com.devalr.framework.components.snackbar.GHSnackBar
import com.devalr.framework.components.snackbar.SnackBarType
import com.devalr.framework.components.snackbar.SnackBarVisualsCustom
import com.devalr.projectdetail.components.ProjectDetailScreenContent
import com.devalr.projectdetail.interactions.Action.DeleteProject
import com.devalr.projectdetail.interactions.Action.EditProject
import com.devalr.projectdetail.interactions.Action.Load
import com.devalr.projectdetail.interactions.Action.Return
import com.devalr.projectdetail.interactions.ErrorType
import com.devalr.projectdetail.interactions.ErrorType.Delete
import com.devalr.projectdetail.interactions.ErrorType.RetrievingDatabase
import com.devalr.projectdetail.interactions.Event
import com.devalr.projectdetail.interactions.Event.NavigateBack
import com.devalr.projectdetail.interactions.Event.NavigateToEditProject
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    viewModel: ProjectDetailViewModel = koinInject(),
    projectId: Long,
    onNavigateToMiniature: (Long) -> Unit,
    onNavigateBack: () -> Unit,
    onCreateMiniature: () -> Unit,
    onEditProject: (Long) -> Unit

) {
    val state = viewModel.uiState.collectAsState().value
    var showConfirmDelete by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onNavigateBack()
                is NavigateToEditProject -> onEditProject(event.projectId)
                is Event.LaunchSnackBarError -> launch {
                    snackBarHostState.currentSnackbarData?.dismiss()
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
    LaunchedEffect(true) { viewModel.onAction(Load(projectId = projectId)) }
    if (showConfirmDelete) {
        ModalBottomSheet(onDismissRequest = { showConfirmDelete = false }) {
            ConfirmBottomSheetContent(
                description = stringResource(com.devalr.framework.R.string.bs_confirm_delete_description),
                okButtonText = stringResource(com.devalr.framework.R.string.btn_delete),
                onConfirmDelete = {
                    showConfirmDelete = false
                    viewModel.onAction(DeleteProject(state.project!!.id))
                },
                onDeny = {
                    showConfirmDelete = false
                }
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                GHSnackBar(snackBarData = data)
            }
        }
    ) { innerPadding ->
        if (state.error) {
            EmptyScreen { viewModel.onAction(Return) }
        } else if (state.projectLoaded && state.project != null) {
            ProjectDetailScreenContent(
                innerPadding = innerPadding,
                project = state.project,
                onNavigateToMiniature = onNavigateToMiniature,
                onCreateMiniature = onCreateMiniature,
                onNavigateBack = {
                    viewModel.onAction(Return)
                },
                onEditProject = {
                    viewModel.onAction(EditProject(projectId = projectId))
                },
                onDeleteProject = {
                    showConfirmDelete = true
                }
            )
        } else {
            LoadingIndicator()
        }
    }
}

private fun getSnackBarMessage(context: Context, error: ErrorType): String =
    when (error) {
        RetrievingDatabase -> context.getString(R.string.error_project_detail_retrieving)
        Delete -> context.getString(R.string.error_project_detail_delete)
    }
