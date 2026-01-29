package com.devalr.greyhunter.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.devalr.createminiature.AddMiniatureScreen
import com.devalr.createproject.AddProjectScreen
import com.devalr.framework.AppTracer
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


@Composable
fun NavHost(tracer: AppTracer) {
    val backStack = remember { mutableStateListOf<Any>(Home) }
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


    NavDisplay(
        backStack = backStack,
        onBack = {
            val topEntry = backStack.lastOrNull()
            if (topEntry !is MiniDetail || !topEntry.onlyUpdate) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = { key ->
            tracer.setScreen(key.toString())
            tracer.log("Navigated to $key")
            when (key) {
                is Home -> NavEntry(key) {
                    HomeScreen(
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
                        }
                    )
                }

                is ProjectDetail -> NavEntry(key) {
                    ProjectDetailScreen(
                        projectId = key.projectId,
                        onNavigateToMiniature = { miniatureId ->
                            backStack.add(MiniDetail(miniatureId = miniatureId))
                        },
                        onCreateMiniature = {
                            backStack.add(AddMiniature(projectId = key.projectId))
                        },
                        onNavigateBack = { backStack.removeLastOrNull() },
                        onEditProject = { projectId ->
                            backStack.add(AddProject(projectId = projectId))
                        }
                    )
                }

                is MiniDetail -> NavEntry(key) {
                    MiniatureDetailScreen(
                        miniatureId = key.miniatureId,
                        onlyUpdate = key.onlyUpdate,
                        onNavigateBack = { backStack.removeLastOrNull() },
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
                        projectId = key.projectId,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

                is AddMiniature -> NavEntry(
                    metadata = addScreenAnimation,
                    key = key
                ) {
                    AddMiniatureScreen(
                        projectId = key.projectId,
                        miniatureId = key.miniatureId,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

                is StartPainting -> NavEntry(key) {
                    StartPaintingScreen(
                        onNavigateBack = { backStack.removeLastOrNull() },
                        onNavigateToPaintMinis = { backStack.add(Painting(minisIds = it)) }
                    )
                }

                is Painting -> NavEntry(key) {
                    PaintingScreen(
                        minisIds = key.minisIds,
                        onNavigateBack = { backStack.removeLastOrNull() },
                        onNavigateToUpdateMiniatures = { miniaturesToUpdate ->
                            val homeIndex = backStack.indexOfFirst { it is Home }
                            if (homeIndex >= 0) {
                                backStack.subList(homeIndex + 1, backStack.size).clear()
                            } else {
                                backStack.clear()
                            }
                            miniaturesToUpdate.forEach { miniatureId ->
                                backStack.add(
                                    MiniDetail(
                                        miniatureId = miniatureId,
                                        onlyUpdate = true
                                    )
                                )
                            }
                        }
                    )
                }

                is Settings -> NavEntry(key) {
                    SettingsScreen(
                        onNavigateBack = { backStack.removeLastOrNull() }
                    )
                }

                else -> NavEntry(Unit) { }
            }
        }
    )
}