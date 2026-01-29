package com.devalr.projectdetail.interactions

sealed interface Event {
    data class NavigateToEditProject(val projectId: Long) : Event
    data class LaunchSnackBarError(val error: ErrorType) : Event
    data object NavigateBack : Event
}