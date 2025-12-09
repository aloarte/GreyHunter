package com.devalr.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.devalr.framework.components.GHTab
import com.devalr.framework.components.LoadingIndicator
import com.devalr.home.components.AppTitle
import com.devalr.home.components.GamificationMessage
import com.devalr.home.components.ProjectsCarousel
import com.devalr.home.components.StartPaint
import com.devalr.home.interactions.Action.OnAppear
import com.devalr.home.interactions.Action.OnStartPainting
import com.devalr.home.interactions.Event.LaunchStartPaintModal
import com.devalr.home.interactions.Event.NavigateToProject
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinInject(),
    onNavigateToProject: (Int) -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LaunchStartPaintModal ->  onNavigateToProject(33)
                    //TODO()
                is NavigateToProject -> onNavigateToProject(event.projectId)
            }


        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear) }

    Scaffold(
        topBar = {
            GHTab()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AppTitle()
            if (state.projectsLoaded) {
                GamificationMessage()
                ProjectsCarousel()
            } else {
                LoadingIndicator()
            }
            StartPaint {
                viewModel.onAction(OnStartPainting)
            }
        }
    }

}