package com.devalr.minidetail.interactions

sealed interface Event {
    data class NavigateToEditMiniature(val miniatureId: Long, val projectId: Long) : Event
    data class LaunchSnackBarError(val error: ErrorType) : Event
    data object NavigateBack : Event
}