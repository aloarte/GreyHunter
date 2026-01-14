package com.devalr.projectdetail.interactions

sealed interface Action {
    data class OnAppear(val projectId: Long) : Action
    data class OnNavigateToEditProject(val projectId: Long) : Action
    data class OnDeleteProject(val projectId: Long) : Action
    data object OnBackPressed : Action
}