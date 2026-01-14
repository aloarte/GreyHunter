package com.devalr.projectdetail

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
import com.devalr.projectdetail.components.ProjectDetailScreenContent
import com.devalr.projectdetail.interactions.Action.OnAppear
import com.devalr.projectdetail.interactions.Action.OnBackPressed
import com.devalr.projectdetail.interactions.Action.OnDeleteProject
import com.devalr.projectdetail.interactions.Action.OnNavigateToEditProject
import com.devalr.projectdetail.interactions.Event.NavigateBack
import com.devalr.projectdetail.interactions.Event.NavigateToEditProject
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    viewModel: ProjectDetailViewModel = koinInject(),
    projectId: Long,
    onNavigateToMiniature: (Long) -> Unit,
    onCreateMiniature: () -> Unit,
    onBackPressed: () -> Unit,
    onEditProject: (Long) -> Unit

) {
    val state = viewModel.uiState.collectAsState().value
    var showConfirmDelete by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onBackPressed()
                is NavigateToEditProject -> onEditProject(event.projectId)
            }

        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear(projectId = projectId)) }
    if (showConfirmDelete) {
        ModalBottomSheet(onDismissRequest = { showConfirmDelete = false }) {
            ConfirmDeleteBottomSheetContent(
                onConfirmDelete = {
                    showConfirmDelete = false
                    viewModel.onAction(OnDeleteProject(state.project!!.id))
                },
                onDeny = {
                    showConfirmDelete = false
                }
            )
        }
    }

    Scaffold(
        topBar = { }
    ) { innerPadding ->
        if (state.projectLoaded && state.project != null) {
            ProjectDetailScreenContent(
                innerPadding = innerPadding,
                project = state.project,
                onNavigateToMiniature = onNavigateToMiniature,
                onCreateMiniature = onCreateMiniature,
                onBackPressed = {
                    viewModel.onAction(OnBackPressed)
                },
                onEditPressed = {
                    viewModel.onAction(OnNavigateToEditProject(projectId = projectId))
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
