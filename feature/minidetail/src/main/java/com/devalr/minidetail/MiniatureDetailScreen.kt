package com.devalr.minidetail

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.devalr.framework.components.GHTab
import com.devalr.framework.components.LoadingIndicator
import com.devalr.minidetail.components.MiniatureDetailScreenContent
import com.devalr.minidetail.interactions.Action.OnAppear
import com.devalr.minidetail.interactions.Action.OnMilestone
import org.koin.compose.koinInject

@Composable
fun MiniatureDetailScreen(
    viewModel: MiniatureDetailViewModel = koinInject(),
    miniatureId: Long
) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->

        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear(miniatureId)) }

    Scaffold(
        topBar = {
            GHTab(projectName = state.parentProject?.name, miniName = state.miniature?.name)
        }
    ) { innerPadding ->

        if (state.miniatureLoaded && state.miniature != null) {
            MiniatureDetailScreenContent(miniature = state.miniature) { type, enabled ->
                viewModel.onAction(OnMilestone(type = type, enable = enabled))
            }
        } else {
            LoadingIndicator()

        }
    }
}
