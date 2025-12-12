package com.devalr.projectdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.GHTab
import com.devalr.projectdetail.interactions.Action.OnAppear
import org.koin.compose.koinInject

@Composable
fun ProjectDetailScreen(
    viewModel: ProjectDetailViewModel = koinInject(),
    projectId: Long,
    onNavigateToMiniature: (Int) -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->

        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear(projectId = projectId)) }

    Scaffold(
        topBar = {
            GHTab(projectName = state.project?.name)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("Opened ${state.project?.name}")
            Text("Opened ${state.project?.minis?.size}")
            GHButton(text = "Open miniature detail") {
                onNavigateToMiniature(22)
            }
        }
    }

}