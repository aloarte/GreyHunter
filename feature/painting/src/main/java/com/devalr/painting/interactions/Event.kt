package com.devalr.painting.interactions

sealed interface Event {
    data object NavigateBack : Event
    data class NavigateToUpdateMiniatures(val miniatureIds: List<Long>) : Event
    data class LaunchSnackBarError(val error: ErrorType) : Event
}