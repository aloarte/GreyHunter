package com.devalr.projectdetail

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.devalr.framework.components.LoadingIndicator
import com.devalr.projectdetail.components.ProjectDetailScreenContent
import com.devalr.projectdetail.interactions.Action.OnAppear
import org.koin.compose.koinInject

@Composable
fun ProjectDetailScreen(
    viewModel: ProjectDetailViewModel = koinInject(),
    projectId: Long,
    onNavigateToMiniature: (Long) -> Unit,
    onCreateMiniature: () -> Unit,
    onBackPressed: () -> Unit

) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->

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
                onBackPressed = {},
                onEditPressed = {}
            )
        } else {
            LoadingIndicator()
        }
    }

}
