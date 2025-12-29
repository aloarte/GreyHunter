package com.devalr.greyhunter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devalr.createminiature.AddMiniatureScreen
import com.devalr.createproject.AddProjectScreen
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.greyhunter.navigation.NavArguments.MINI_ID
import com.devalr.greyhunter.navigation.NavArguments.PROJECT_ID
import com.devalr.greyhunter.navigation.NavScreen
import com.devalr.home.HomeScreen
import com.devalr.minidetail.MiniatureDetailScreen
import com.devalr.projectdetail.ProjectDetailScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreyHunterTheme {
                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = true
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NavScreen.Home.route
                    ) {
                        composable(route = NavScreen.Home.route) {
                            HomeScreen(
                                onNavigateToProject = { projectId ->
                                    navController.navigate("${NavScreen.ProjectDetail.route}/$projectId")
                                },
                                onNavigateToAddProject = {
                                    navController.navigate(NavScreen.AddProject.route)
                                })
                        }

                        composable(
                            route = NavScreen.AddProject.route,
                            enterTransition = {
                                slideIntoContainer(
                                    towards = Up,
                                    animationSpec = tween(500)
                                )
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = Down,
                                    animationSpec = tween(500)
                                )
                            }
                        ) {
                            AddProjectScreen(onBackPressed = { navController.popBackStack() })
                        }

                        composable(
                            route = "${NavScreen.ProjectDetail.route}/{projectId}",
                            arguments = listOf(navArgument(PROJECT_ID) { type = NavType.LongType })
                        ) { backStackEntry ->
                            val projectId = backStackEntry.arguments?.getLong(PROJECT_ID) ?: 0

                            ProjectDetailScreen(
                                projectId = projectId,
                                onNavigateToMiniature = { miniatureId ->
                                    navController.navigate("${NavScreen.MiniDetail.route}/$miniatureId")
                                },
                                onCreateMiniature = {
                                    navController.navigate("${NavScreen.AddMiniature.route}/$projectId")
                                },
                                onBackPressed = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = "${NavScreen.AddMiniature.route}/{projectId}/{miniatureId}",
                            arguments = listOf(
                                navArgument(PROJECT_ID) { type = NavType.LongType },
                                navArgument(MINI_ID) { type = NavType.LongType }
                            ),
                            enterTransition = {
                                slideIntoContainer(towards = Up, animationSpec = tween(500))
                            },
                            popExitTransition = {
                                slideOutOfContainer(towards = Down, animationSpec = tween(500))
                            }
                        ) { backStackEntry ->
                            val projectId = backStackEntry.arguments?.getLong(PROJECT_ID) ?: -1
                            val miniatureId = backStackEntry.arguments?.getLong(PROJECT_ID) ?: -1
                            AddMiniatureScreen(
                                projectId = projectId,
                                miniatureId = miniatureId,
                                onBackPressed = { navController.popBackStack() })
                        }

                        composable(
                            route = "${NavScreen.MiniDetail.route}/{miniatureId}",
                            arguments = listOf(navArgument(MINI_ID) {
                                type = NavType.LongType
                            })
                        ) { backStackEntry ->
                            val miniatureId = backStackEntry.arguments?.getLong(MINI_ID) ?: 0
                            MiniatureDetailScreen(
                                miniatureId = miniatureId,
                                onBackPressed = { navController.popBackStack() },
                                onEditMiniaturePressed = { miniatureId, projectId ->
                                    navController.navigate("${NavScreen.AddMiniature.route}/$projectId/$miniatureId")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
