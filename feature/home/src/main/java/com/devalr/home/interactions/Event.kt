package com.devalr.home.interactions

sealed interface Event {
    data object NavigateStartPaint : Event
    data class NavigateToProject(val projectId: Long) : Event
    data object NavigateToAddProject : Event
}