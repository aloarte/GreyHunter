package com.devalr.startpainting

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.devalr.framework.components.anim.LoadingIndicator
import com.devalr.startpainting.components.StartPaintingScreenContent
import com.devalr.startpainting.interactions.Action
import com.devalr.startpainting.interactions.Action.OnSelectMiniature
import com.devalr.startpainting.interactions.Action.OnStartPainting
import com.devalr.startpainting.interactions.Event.NavigateBack
import com.devalr.startpainting.interactions.Event.NavigatePaintMiniatures
import org.koin.compose.koinInject

@Composable
fun StartPaintingScreen(
    viewModel: StartPaintingViewModel = koinInject(),
    onNavigateToPaintMinis:(List<Long>)->Unit,
    onBackPressed: () -> Unit

) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onBackPressed()
                is NavigatePaintMiniatures -> onNavigateToPaintMinis(event.miniatures)
            }

        }
    }
    LaunchedEffect(true) { viewModel.onAction(Action.OnAppear) }

    Scaffold(
        topBar = { }
    ) { innerPadding ->
        if (state.projectsLoaded) {
            if (state.projectList.isNotEmpty()) {
                StartPaintingScreenContent(
                    innerPadding = innerPadding,
                    projectList = state.projectList,
                    buttonEnabled = state.paintButtonEnabled,
                    onBackPressed = onBackPressed,
                    onStartPainting = {
                        viewModel.onAction(OnStartPainting)
                    },
                    onMiniatureSelected = { miniature ->
                        viewModel.onAction(OnSelectMiniature(miniature))
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
