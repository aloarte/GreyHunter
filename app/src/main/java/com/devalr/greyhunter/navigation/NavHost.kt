package com.devalr.greyhunter.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.devalr.createminiature.AddMiniatureScreen
import com.devalr.createproject.AddProjectScreen
import com.devalr.greyhunter.navigation.NavScreen.AddMiniature
import com.devalr.greyhunter.navigation.NavScreen.AddProject
import com.devalr.greyhunter.navigation.NavScreen.Home
import com.devalr.greyhunter.navigation.NavScreen.MiniDetail
import com.devalr.greyhunter.navigation.NavScreen.ProjectDetail
import com.devalr.greyhunter.navigation.NavScreen.Settings
import com.devalr.home.HomeScreen
import com.devalr.minidetail.MiniatureDetailScreen
import com.devalr.projectdetail.ProjectDetailScreen


@Composable
fun NavHost() {
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
        onBack = { backStack.removeLastOrNull() },

        entryProvider = { key ->
            when (key) {
                is Home -> NavEntry(key) {
                    HomeScreen(
                        onNavigateToProject = { projectId ->
                            backStack.add(ProjectDetail(projectId = projectId))
                        },
                        onNavigateToAddProject = {
                            backStack.add(AddProject())
                        })
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
                        onBackPressed = { backStack.removeLastOrNull() },
                        onEditProject = { projectId ->
                            backStack.add(AddProject(projectId = projectId))
                        }
                    )
                }

                is MiniDetail -> NavEntry(key) {
                    MiniatureDetailScreen(
                        miniatureId = key.miniatureId,
                        onBackPressed = { backStack.removeLastOrNull() },
                        onEditMiniaturePressed = { miniatureId, projectId ->
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
                        onBackPressed = { backStack.removeLastOrNull() }
                    )
                }

                is AddMiniature -> NavEntry(
                    metadata = addScreenAnimation,
                    key = key
                ) {
                    AddMiniatureScreen(
                        projectId = key.projectId,
                        miniatureId = key.miniatureId,
                        onBackPressed = { backStack.removeLastOrNull() }
                    )
                }

                is Settings -> NavEntry(key) {
                }

                else -> NavEntry(Unit) { }
            }
        }
    )
}