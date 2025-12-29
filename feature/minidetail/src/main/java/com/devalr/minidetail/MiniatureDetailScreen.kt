package com.devalr.minidetail

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.devalr.framework.components.LoadingIndicator
import com.devalr.minidetail.components.MiniatureDetailScreenContent
import com.devalr.minidetail.interactions.Action.OnAppear
import com.devalr.minidetail.interactions.Action.OnBackPressed
import com.devalr.minidetail.interactions.Action.OnMilestone
import com.devalr.minidetail.interactions.Action.OnNavigateToEditMiniature
import com.devalr.minidetail.interactions.Event.NavigateBack
import com.devalr.minidetail.interactions.Event.NavigateToEditMiniature
import org.koin.compose.koinInject

@Composable
fun MiniatureDetailScreen(
    viewModel: MiniatureDetailViewModel = koinInject(),
    miniatureId: Long,
    onBackPressed: () -> Unit,
    onEditMiniaturePressed: (Long, Long) -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
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

    Scaffold(
        topBar = {}
    ) { innerPadding ->
        if (state.miniatureLoaded && state.miniature != null && state.parentProject != null) {
            MiniatureDetailScreenContent(
                innerPadding = innerPadding,
                miniature = state.miniature,
                onBackPressed = {
                    viewModel.onAction(OnBackPressed)
                },
                onEditPressed = {
                    viewModel.onAction(
                        OnNavigateToEditMiniature(
                            state.miniature.id,
                            state.miniature.projectId
                        )
                    )
                },
                onMilestone = { type, enabled ->
                    viewModel.onAction(OnMilestone(type = type, enable = enabled))
                }
            )
        } else {
            LoadingIndicator()
        }
    }
}
