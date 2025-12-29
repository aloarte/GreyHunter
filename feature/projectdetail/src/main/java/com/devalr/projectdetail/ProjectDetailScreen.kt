package com.devalr.projectdetail

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.devalr.framework.components.LoadingIndicator
import com.devalr.projectdetail.components.ProjectDetailScreenContent
import com.devalr.projectdetail.interactions.Action
import com.devalr.projectdetail.interactions.Action.OnAppear
import com.devalr.projectdetail.interactions.Event.NavigateBack
import com.devalr.projectdetail.interactions.Event.NavigateToEditProject
import org.koin.compose.koinInject

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
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onBackPressed()
                is NavigateToEditProject -> onEditProject(event.projectId)
            }

        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear(projectId = projectId)) }

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
                    viewModel.onAction(Action.OnBackPressed)
                },
                onEditPressed = {
                    viewModel.onAction(Action.OnNavigateToEditProject(projectId = projectId))
                }
            )
        } else {
            LoadingIndicator()
        }
    }

}
