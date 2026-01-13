package com.devalr.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.devalr.framework.components.LoadingIndicator
import com.devalr.home.components.screen.AppTitle
import com.devalr.home.components.screen.GamificationMessage
import com.devalr.home.components.screen.LastUpdated
import com.devalr.home.components.screen.ProjectsCarousel
import com.devalr.home.components.screen.StartPaint
import com.devalr.home.interactions.Action.OnAddProject
import com.devalr.home.interactions.Action.OnAppear
import com.devalr.home.interactions.Action.OnOpenMiniatureDetail
import com.devalr.home.interactions.Action.OnOpenProjectDetail
import com.devalr.home.interactions.Action.OnStartPainting
import com.devalr.home.interactions.Event.NavigateStartPaint
import com.devalr.home.interactions.Event.NavigateToAddProject
import com.devalr.home.interactions.Event.NavigateToMiniature
import com.devalr.home.interactions.Event.NavigateToProject
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinInject(),
    onNavigateToProject: (Long) -> Unit,
    onNavigateToMiniature: (Long) -> Unit,
    onNavigateToAddProject: () -> Unit,
    onNavigateToStartPainting: () -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateStartPaint -> onNavigateToStartPainting()
                is NavigateToProject -> onNavigateToProject(event.projectId)
                is NavigateToAddProject -> onNavigateToAddProject()
                is NavigateToMiniature -> onNavigateToMiniature(event.miniatureId)
            }
        }
    }
    LaunchedEffect(true) { viewModel.onAction(OnAppear) }

    Scaffold(
        topBar = {}
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AppTitle()
            if (state.loaded) {
                ProjectsCarousel(
                    projects = state.projects,
                    onProjectClicked = { projectId ->
                        viewModel.onAction(OnOpenProjectDetail(projectId = projectId))
                    },
                    onCreateProject = {
                        viewModel.onAction(OnAddProject)
                    }
                )
                GamificationMessage()
                LastUpdated(
                    project = state.lastUpdatedProject,
                    miniature = state.lastUpdatedMini,
                    onProjectClicked = {
                        viewModel.onAction(OnOpenProjectDetail(projectId = it))
                    },
                    onMiniatureClicked = {
                        viewModel.onAction(OnOpenMiniatureDetail(miniatureId = it))
                    }
                )

            } else {
                LoadingIndicator()
            }
            StartPaint {
                viewModel.onAction(OnStartPainting)
            }
        }
    }

}