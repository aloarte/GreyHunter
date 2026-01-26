package com.devalr.startpainting.interactions

sealed interface Event {
    data object NavigateBack : Event
    data class NavigateToPaintMiniatures(val miniatures: List<Long>) : Event
}