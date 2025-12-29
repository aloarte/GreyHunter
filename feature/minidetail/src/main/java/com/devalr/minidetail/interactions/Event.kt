package com.devalr.minidetail.interactions

sealed interface Event {
    data class NavigateToEditMiniature(val miniatureId: Long, val projectId: Long) : Event
    data object NavigateBack : Event

}