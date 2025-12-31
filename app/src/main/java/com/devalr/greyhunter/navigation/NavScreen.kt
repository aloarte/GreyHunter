package com.devalr.greyhunter.navigation

sealed interface NavScreen {
    data object Home : NavScreen
    data class ProjectDetail(val projectId: Long) : NavScreen
    data class AddProject(val projectId: Long? = null) : NavScreen
    data class MiniDetail(val miniatureId: Long) : NavScreen
    data class AddMiniature(val projectId: Long, val miniatureId: Long? = null) : NavScreen
    data object Settings : NavScreen
}