package com.devalr.projectdetail.interactions

sealed interface Event {
    data class NavigateToEditProject(val projectId: Long) : Event
    data object NavigateBack : Event
}