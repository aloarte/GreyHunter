package com.devalr.home.interactions

sealed interface Event {
    data object NavigateToStartPaint : Event
    data class NavigateToProject(val projectId: Long) : Event
    data class NavigateToMiniature(val miniatureId: Long) : Event
    data object NavigateToAddProject : Event
    data object NavigateToSettings : Event

    data class LaunchSnackBarError(val error: ErrorType) :Event

}