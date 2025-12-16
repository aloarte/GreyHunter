package com.devalr.greyhunter.navigation

sealed class NavScreen(val route: String) {
    data object Home : NavScreen("Home")
    data object ProjectDetail : NavScreen("ProjectDetail")
    data object AddProject : NavScreen("AddProject")
    data object MiniDetail : NavScreen("MiniDetail")
    data object AddMiniature : NavScreen("AddMiniature")
    data object Settings : NavScreen("Settings")
}