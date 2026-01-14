package com.devalr.minidetail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.devalr.framework.components.LoadingIndicator
import com.devalr.framework.components.bottomsheet.ConfirmDeleteBottomSheetContent
import com.devalr.minidetail.components.MiniatureDetailScreenContent
import com.devalr.minidetail.interactions.Action.OnAppear
import com.devalr.minidetail.interactions.Action.OnBackPressed
import com.devalr.minidetail.interactions.Action.OnDeleteMiniature
import com.devalr.minidetail.interactions.Action.OnMilestone
import com.devalr.minidetail.interactions.Action.OnNavigateToEditMiniature
import com.devalr.minidetail.interactions.Event.NavigateBack
import com.devalr.minidetail.interactions.Event.NavigateToEditMiniature
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniatureDetailScreen(
    viewModel: MiniatureDetailViewModel = koinInject(),
    miniatureId: Long,
    onlyUpdate: Boolean,
    onBackPressed: () -> Unit,
    onEditMiniaturePressed: (Long, Long) -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
    var showConfirmDelete by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onBackPressed()
                is NavigateToEditMiniature -> onEditMiniaturePressed(
                    event.miniatureId,
                    event.projectId
                )
            }

        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear(miniatureId)) }
    if (showConfirmDelete) {
        ModalBottomSheet(onDismissRequest = { showConfirmDelete = false }) {
            ConfirmDeleteBottomSheetContent(
                onConfirmDelete = {
                    showConfirmDelete = false
                    viewModel.onAction(OnDeleteMiniature(state.miniature!!.id))
                },
                onDeny = {
                    showConfirmDelete = false
                }
            )
        }
    }
    Scaffold(
        topBar = {}
    ) { innerPadding ->
        if (state.miniatureLoaded && state.miniature != null && state.parentProject != null) {
            MiniatureDetailScreenContent(
                innerPadding = innerPadding,
                onlyUpdate = onlyUpdate,
                miniature = state.miniature,
                onBackPressed = {
                    viewModel.onAction(OnBackPressed)
                },
                onEditPressed = {
                    viewModel.onAction(
                        OnNavigateToEditMiniature(
                            miniatureId = state.miniature.id,
                            projectId = state.miniature.projectId
                        )
                    )
                },
                onMilestone = { type, enabled ->
                    viewModel.onAction(OnMilestone(type = type, enable = enabled))
                },
                onDeletePressed = {
                    showConfirmDelete = true
                }
            )
        } else {
            LoadingIndicator()
        }
    }
}
