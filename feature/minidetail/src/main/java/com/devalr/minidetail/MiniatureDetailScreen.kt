package com.devalr.minidetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.GHImage
import com.devalr.framework.components.GHTab
import com.devalr.framework.components.LoadingIndicator
import com.devalr.minidetail.components.MiniatureMilestones
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("Opened $miniatureId")

            if (state.miniatureLoaded && state.miniature != null) {
                GHImage(imageUri = state.miniature.imageUri, size = 300.dp)
                MiniatureMilestones(state.miniature.completion) { type, enabled ->
                    viewModel.onAction(OnMilestone(type = type, enable = enabled))
                }
            } else {
                LoadingIndicator()
            }

        }
    }

}
