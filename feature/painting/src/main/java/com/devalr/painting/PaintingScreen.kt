package com.devalr.painting

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.devalr.framework.components.anim.LoadingIndicator
import com.devalr.painting.components.PaintingScreenContent
import com.devalr.painting.interactions.Action.FinishPainting
import com.devalr.painting.interactions.Action.Load
import com.devalr.painting.interactions.Action.Return
import com.devalr.painting.interactions.Event.NavigateBack
import com.devalr.painting.interactions.Event.NavigateToUpdateMiniatures
import org.koin.compose.koinInject

@Composable
fun PaintingScreen(
    viewModel: PaintingViewModel = koinInject(),
    minisIds: List<Long>,
    onNavigateBack: () -> Unit,
    onNavigateToUpdateMiniatures: (List<Long>) -> Unit

) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onNavigateBack()
                is NavigateToUpdateMiniatures -> onNavigateToUpdateMiniatures(event.miniatureIds)
            }

        }
    }
    LaunchedEffect(true) { viewModel.onAction(Load(minisIds)) }


    Scaffold(
        topBar = { }
    ) { innerPadding ->
        if (state.minisLoaded) {
            if (state.miniatures.isNotEmpty()) {
                PaintingScreenContent(
                    innerPadding = innerPadding,
                    miniatures = state.miniatures,
                    onNavigateBack = { viewModel.onAction(Return) },
                    onFinishPainting = { viewModel.onAction(FinishPainting(minisIds)) }
                )
            } else {
                //TODO: Display error
            }

        } else {
            LoadingIndicator()
        }
    }
}
