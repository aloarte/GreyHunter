package com.devalr.greyhunter.navigation

sealed class NavScreen(val route: String) {
    data object Home : NavScreen("Home")
    data object ProjectDetail : NavScreen("ProjectDetail")
    data object MiniDetail : NavScreen("MiniDetail")
    data object Settings : NavScreen("Settings")
}