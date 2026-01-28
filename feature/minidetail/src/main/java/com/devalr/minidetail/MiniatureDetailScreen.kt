package com.devalr.minidetail

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
import com.devalr.framework.components.snackbar.GHSnackBar
import com.devalr.framework.components.snackbar.SnackBarType
import com.devalr.framework.components.snackbar.SnackBarVisualsCustom
import com.devalr.minidetail.components.MiniatureDetailScreenContent
import com.devalr.minidetail.interactions.Action
import com.devalr.minidetail.interactions.Action.DeleteMiniature
import com.devalr.minidetail.interactions.Action.Load
import com.devalr.minidetail.interactions.Action.Return
import com.devalr.minidetail.interactions.Action.UpdateMilestone
import com.devalr.minidetail.interactions.ErrorType
import com.devalr.minidetail.interactions.ErrorType.CompletePreviousSteps
import com.devalr.minidetail.interactions.ErrorType.Delete
import com.devalr.minidetail.interactions.ErrorType.EmptyMiniature
import com.devalr.minidetail.interactions.ErrorType.RetrievingDatabase
import com.devalr.minidetail.interactions.Event.LaunchSnackBarError
import com.devalr.minidetail.interactions.Event.NavigateBack
import com.devalr.minidetail.interactions.Event.NavigateToEditMiniature
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniatureDetailScreen(
    viewModel: MiniatureDetailViewModel = koinInject(),
    miniatureId: Long,
    onlyUpdate: Boolean,
    onNavigateBack: () -> Unit,
    onEditMiniature: (Long, Long) -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
    var showConfirmDelete by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is NavigateToEditMiniature -> onEditMiniature(
                    event.miniatureId,
                    event.projectId
                )

                NavigateBack -> onNavigateBack()
                is LaunchSnackBarError -> launch {
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
    LaunchedEffect(true) { viewModel.onAction(Load(miniatureId)) }
    if (showConfirmDelete) {
        ModalBottomSheet(onDismissRequest = { showConfirmDelete = false }) {
            ConfirmBottomSheetContent(
                description = stringResource(com.devalr.framework.R.string.bs_confirm_delete_description),
                okButtonText = stringResource(com.devalr.framework.R.string.btn_delete),
                onConfirmDelete = {
                    showConfirmDelete = false
                    viewModel.onAction(DeleteMiniature(state.miniature!!.id))
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
        if (state.miniatureLoaded && state.miniature != null && state.parentProject != null) {
            MiniatureDetailScreenContent(
                innerPadding = innerPadding,
                onlyUpdate = onlyUpdate,
                parentProjectName = state.parentProject.name,
                miniature = state.miniature,
                onNavigateBack = {
                    viewModel.onAction(Return)
                },
                onEdit = {
                    viewModel.onAction(
                        Action.EditMiniature(
                            miniatureId = state.miniature.id,
                            projectId = state.miniature.projectId
                        )
                    )
                },
                onUpdateMilestone = { type, enabled ->
                    viewModel.onAction(UpdateMilestone(type = type, enable = enabled))
                },
                onDelete = {
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
        RetrievingDatabase -> context.getString(R.string.error_mini_detail_retrieving)
        CompletePreviousSteps -> context.getString(R.string.error_mini_detail_previous_steps)
        EmptyMiniature -> context.getString(R.string.error_mini_detail_empty_mini)
        Delete -> context.getString(R.string.error_mini_detail_delete)
    }
