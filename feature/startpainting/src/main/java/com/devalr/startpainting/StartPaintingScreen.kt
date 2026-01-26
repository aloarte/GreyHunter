package com.devalr.startpainting

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.devalr.framework.components.anim.LoadingIndicator
import com.devalr.startpainting.components.StartPaintingScreenContent
import com.devalr.startpainting.interactions.Action
import com.devalr.startpainting.interactions.Action.SelectMiniature
import com.devalr.startpainting.interactions.Action.StartPainting
import com.devalr.startpainting.interactions.Event.NavigateBack
import com.devalr.startpainting.interactions.Event.NavigateToPaintMiniatures
import org.koin.compose.koinInject

@Composable
fun StartPaintingScreen(
    viewModel: StartPaintingViewModel = koinInject(),
    onNavigateToPaintMinis: (List<Long>) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onNavigateBack()
                is NavigateToPaintMiniatures -> onNavigateToPaintMinis(event.miniatures)
            }

        }
    }
    LaunchedEffect(true) { viewModel.onAction(Action.Load) }

    Scaffold(
        topBar = { }
    ) { innerPadding ->
        if (state.projectsLoaded) {
            if (state.projectList.isNotEmpty()) {
                StartPaintingScreenContent(
                    innerPadding = innerPadding,
                    projectList = state.projectList,
                    buttonEnabled = state.paintButtonEnabled,
                    onNavigateBack = onNavigateBack,
                    onStartPainting = {
                        viewModel.onAction(StartPainting)
                    },
                    onSelectMiniature = { miniature ->
                        viewModel.onAction(SelectMiniature(miniature))
                    }
                )

            } else {
                //TODO: Display error
            }

        } else {
            LoadingIndicator()
        }
    }
}
