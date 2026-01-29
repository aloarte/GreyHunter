package com.devalr.home

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.devalr.framework.components.anim.LoadingIndicator
import com.devalr.framework.components.empty.EmptyScreen
import com.devalr.framework.components.snackbar.GHSnackBar
import com.devalr.framework.components.snackbar.SnackBarType
import com.devalr.framework.components.snackbar.SnackBarVisualsCustom
import com.devalr.home.components.HomeScreenContent
import com.devalr.home.components.screen.AppTitle
import com.devalr.home.interactions.Action.AddProject
import com.devalr.home.interactions.Action.Load
import com.devalr.home.interactions.Action.OpenMiniatureDetail
import com.devalr.home.interactions.Action.OpenProjectDetail
import com.devalr.home.interactions.Action.OpenSettings
import com.devalr.home.interactions.Action.StartPainting
import com.devalr.home.interactions.ErrorType
import com.devalr.home.interactions.Event.LaunchSnackBarError
import com.devalr.home.interactions.Event.NavigateToAddProject
import com.devalr.home.interactions.Event.NavigateToMiniature
import com.devalr.home.interactions.Event.NavigateToProject
import com.devalr.home.interactions.Event.NavigateToSettings
import com.devalr.home.interactions.Event.NavigateToStartPaint
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinInject(),
    onNavigateToProject: (Long) -> Unit,
    onNavigateToMiniature: (Long) -> Unit,
    onNavigateToAddProject: () -> Unit,
    onNavigateToStartPainting: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateToStartPaint -> onNavigateToStartPainting()
                is NavigateToProject -> onNavigateToProject(event.projectId)
                is NavigateToAddProject -> onNavigateToAddProject()
                is NavigateToMiniature -> onNavigateToMiniature(event.miniatureId)
                is NavigateToSettings -> onNavigateToSettings()
                is LaunchSnackBarError -> launch {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(
                        SnackBarVisualsCustom(
                            duration = SnackbarDuration.Short,
                            message = getSnackBarMessage(context, event.error),
                            type = SnackBarType.ERROR
                        )
                    )
                }
            }
        }
    }
    LaunchedEffect(true) { viewModel.onAction(Load) }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                GHSnackBar(snackBarData = data)
            }
        },
        floatingActionButton = {
            if (state.loaded && state.projects.any { (it.hasMinis()) }) {
                FloatingActionButton(
                    onClick = { viewModel.onAction(StartPainting) },
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
            if (state.error) {
                AppTitle(onNavigateToSettings = { viewModel.onAction(OpenSettings) })
                EmptyScreen()
            } else if (state.loaded) {
                HomeScreenContent(
                    projects = state.projects,
                    almostDoneProjects = state.almostDoneProjects,
                    lastUpdatedMinis = state.lastUpdatedMinis,
                    gamificationMessage = state.gamificationSentence,
                    onOpenProjectDetail = { projectId ->
                        viewModel.onAction(OpenProjectDetail(projectId = projectId))
                    },
                    onOpenMiniatureDetail = { miniatureId ->
                        viewModel.onAction(OpenMiniatureDetail(miniatureId = miniatureId))
                    },
                    onAddProject = { viewModel.onAction(AddProject) },
                    onNavigateToSettings = { viewModel.onAction(OpenSettings) }
                )
            } else {
                AppTitle(onNavigateToSettings = { viewModel.onAction(OpenSettings) })
                LoadingIndicator()
            }
        }
    }
}

private fun getSnackBarMessage(context: Context, error: ErrorType): String =
    when (error) {
        ErrorType.RetrievingDatabase -> context.getString(R.string.error_home_retrieving)
    }
