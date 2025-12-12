package com.devalr.greyhunter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.greyhunter.navigation.NavArguments.MINI_ID
import com.devalr.greyhunter.navigation.NavArguments.PROJECT_ID
import com.devalr.greyhunter.navigation.NavScreen
import com.devalr.home.HomeScreen
import com.devalr.minidetail.MiniatureDetailScreen
import com.devalr.projectdetail.ProjectDetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreyHunterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NavScreen.Home.route
                    ) {
                        composable(NavScreen.Home.route) {
                            HomeScreen { projectId ->
                                navController.navigate("${NavScreen.ProjectDetail.route}/$projectId")
                            }
                        }

                        composable(
                            route = "${NavScreen.ProjectDetail.route}/{projectId}",
                            arguments = listOf(navArgument(PROJECT_ID) { type = NavType.LongType })
                        ) { backStackEntry ->
                            val projectId = backStackEntry.arguments?.getLong(PROJECT_ID) ?: 0

                            ProjectDetailScreen(projectId = projectId) { miniatureId ->
                                navController.navigate("${NavScreen.MiniDetail.route}/$miniatureId")
                            }
                        }

                        composable(
                            route = "${NavScreen.MiniDetail.route}/{miniatureId}",
                            arguments = listOf(navArgument(MINI_ID) {
                                type = NavType.IntType
                            })
                        ) { backStackEntry ->
                            val miniatureId = backStackEntry.arguments?.getInt(MINI_ID) ?: 0

                            MiniatureDetailScreen(miniatureId = miniatureId)
                        }
                    }
                }
            }
        }
    }
}
