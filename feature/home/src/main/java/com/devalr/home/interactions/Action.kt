package com.devalr.home.interactions

sealed interface Action {
    data object OnAppear : Action
    data object OnStartPainting : Action
    data class OnOpenProjectDetail(val projectId: Long) : Action
    data object OnAddProject : Action
}