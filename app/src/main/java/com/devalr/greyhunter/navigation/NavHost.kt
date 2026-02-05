package com.devalr.greyhunter.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.devalr.createminiature.AddMiniatureScreen
import com.devalr.createproject.AddProjectScreen
import com.devalr.framework.AppTracer
import com.devalr.framework.components.snackbar.SnackBarType
import com.devalr.framework.components.snackbar.SnackBarVisualsCustom
import com.devalr.greyhunter.R
import com.devalr.greyhunter.navigation.NavScreen.AddMiniature
import com.devalr.greyhunter.navigation.NavScreen.AddProject
import com.devalr.greyhunter.navigation.NavScreen.Home
import com.devalr.greyhunter.navigation.NavScreen.MiniDetail
import com.devalr.greyhunter.navigation.NavScreen.Painting
import com.devalr.greyhunter.navigation.NavScreen.ProjectDetail
import com.devalr.greyhunter.navigation.NavScreen.Settings
import com.devalr.greyhunter.navigation.NavScreen.StartPainting
import com.devalr.home.HomeScreen
import com.devalr.minidetail.MiniatureDetailScreen
import com.devalr.painting.PaintingScreen
import com.devalr.projectdetail.ProjectDetailScreen
import com.devalr.settings.SettingsScreen
import com.devalr.startpainting.StartPaintingScreen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavHost(
    snackBarHostState: SnackbarHostState,
    tracer: AppTracer
) {
    val scope = rememberCoroutineScope()
    val backStack = remember { mutableStateListOf<Any>(Home) }
    val context = LocalContext.current
    val addScreenAnimation = NavDisplay.transitionSpec {
        slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(300)
        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
    } + NavDisplay.popTransitionSpec {
        EnterTransition.None togetherWith
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(300)
                )
    } + NavDisplay.predictivePopTransitionSpec {
        EnterTransition.None togetherWith
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(300)
                )
    }
    // This case should not happen, but it's added to avoid potential crashes
    if (backStack.isEmpty()) {
        backStack.add(Home)
    }

    NavDisplay(
        backStack = backStack,
        onBack = {
            val topEntry = backStack.lastOrNull()
            when {
                topEntry !is MiniDetail || !topEntry.onlyUpdate -> backStack.popBackStackSafe()
                else -> scope.launch {
                    snackBarHostState.showSnackbar(
                        SnackBarVisualsCustom(
                            duration = SnackbarDuration.Short,
                            message = context.getString(R.string.snackbar_error_back_mini_detail),
                            type = SnackBarType.ERROR
                        )
                    )
                }
            }
        },
        entryProvider = { key ->
            tracer.setScreen(key.toString())
            tracer.log("Navigated to $key")
            when (key) {
                is Home -> NavEntry(key) {
                    HomeScreen(
                        snackBarHostState = snackBarHostState,
                        onNavigateToProject = { projectId ->
                            backStack.add(ProjectDetail(projectId = projectId))
                        },
                        onNavigateToMiniature = { miniatureId ->
                            backStack.add(MiniDetail(miniatureId = miniatureId))
                        },
                        onNavigateToStartPainting = {
                            backStack.add(StartPainting)
                        },
                        onNavigateToAddProject = {
                            backStack.add(AddProject())
                        },
                        onNavigateToSettings = {
                            backStack.add(Settings)
                        },
                        onExit = { backStack.popBackStackSafe() }
                    )
                }

                is ProjectDetail -> NavEntry(key) {
                    ProjectDetailScreen(
                        snackBarHostState = snackBarHostState,
                        projectId = key.projectId,
                        onNavigateToMiniature = { miniatureId ->
                            backStack.add(MiniDetail(miniatureId = miniatureId))
                        },
                        onCreateMiniature = {
                            backStack.add(AddMiniature(projectId = key.projectId))
                        },
                        onNavigateBack = { backStack.popBackStackSafe() },
                        onEditProject = { projectId ->
                            backStack.add(AddProject(projectId = projectId))
                        }
                    )
                }

                is MiniDetail -> NavEntry(key) {
                    MiniatureDetailScreen(
                        snackBarHostState = snackBarHostState,
                        miniatureId = key.miniatureId,
                        onlyUpdate = key.onlyUpdate,
                        onNavigateBack = { backStack.popBackStackSafe() },
                        onEditMiniature = { miniatureId, projectId ->
                            backStack.add(
                                AddMiniature(
                                    projectId = projectId,
                                    miniatureId = miniatureId
                                )
                            )
                        }
                    )
                }

                is AddProject -> NavEntry(
                    metadata = addScreenAnimation,
                    key = key
                ) {
                    AddProjectScreen(
                        snackBarHostState = snackBarHostState,
                        projectId = key.projectId,
                        onBack = { backStack.popBackStackSafe() }
                    )
                }

                is AddMiniature -> NavEntry(
                    metadata = addScreenAnimation,
                    key = key
                ) {
                    AddMiniatureScreen(
                        snackBarHostState = snackBarHostState,
                        projectId = key.projectId,
                        miniatureId = key.miniatureId,
                        onBack = { backStack.popBackStackSafe() }
                    )
                }

                is StartPainting -> NavEntry(key) {
                    StartPaintingScreen(
                        snackBarHostState = snackBarHostState,
                        onNavigateBack = { backStack.popBackStackSafe() },
                        onNavigateToPaintMinis = { backStack.add(Painting(minisIds = it)) }
                    )
                }

                is Painting -> NavEntry(key) {
                    PaintingScreen(
                        snackBarHostState = snackBarHostState,
                        minisIds = key.minisIds,
                        onNavigateBack = { backStack.popBackStackSafe() },
                        onNavigateToUpdateMiniatures = { miniaturesToUpdate ->
                            backStack.apply {
                                retainAll { it is Home }
                                if (isEmpty()) {
                                    add(Home)
                                }
                                /* When going back from PaintingScreen the detail of each of N mini painted is opened,
                                * so it must display this N screens instead of HomeScreen */
                                miniaturesToUpdate.forEach { miniatureId ->
                                    add(
                                        MiniDetail(
                                            miniatureId = miniatureId,
                                            onlyUpdate = true
                                        )
                                    )
                                }
                            }
                        }
                    )
                }

                is Settings -> NavEntry(key) {
                    SettingsScreen(
                        snackBarHostState = snackBarHostState,
                        onNavigateBack = { backStack.popBackStackSafe() }
                    )
                }

                else -> NavEntry(Unit) { }
            }
        }
    )

}

private fun <T> MutableList<T>.popBackStackSafe() {
    if (size > 1) {
        removeLastOrNull()
    }
}