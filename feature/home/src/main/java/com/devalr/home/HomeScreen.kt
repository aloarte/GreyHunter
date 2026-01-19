package com.devalr.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.devalr.framework.components.anim.LoadingIndicator
import com.devalr.home.components.HomeScreenContent
import com.devalr.home.components.screen.AppTitle
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
        topBar = {},
        floatingActionButton = {
            if (state.loaded && state.projects.any { (it.hasMinis()) }) {
                FloatingActionButton(
                    onClick = { viewModel.onAction(OnStartPainting) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        painter = painterResource(com.devalr.framework.R.drawable.ic_paint),
                        contentDescription = "Start Painting"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.loaded) {
                HomeScreenContent(
                    projects = state.projects,
                    almostDoneProjects = state.almostDoneProjects,
                    lastUpdatedMinis = state.lastUpdatedMinis,
                    gamificationMessage = state.gamificationSentence,
                    onOpenProjectDetail = { projectId ->
                        viewModel.onAction(OnOpenProjectDetail(projectId = projectId))
                    },
                    onOpenMiniatureDetail = { miniatureId ->
                        viewModel.onAction(OnOpenMiniatureDetail(miniatureId = miniatureId))
                    },
                    onAddProject = {
                        viewModel.onAction(OnAddProject)
                    }
                )
            } else {
                AppTitle()
                LoadingIndicator()
            }
        }
    }
}