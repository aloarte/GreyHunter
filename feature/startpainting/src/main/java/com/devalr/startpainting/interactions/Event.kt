package com.devalr.startpainting.interactions


sealed interface Event {
    data object NavigateBack : Event
    data class NavigateToPaintMiniatures(val miniatures: List<Long>) : Event
    data class LaunchErrorSnackBar(val errorType: ErrorType) : Event
}