package com.devalr.createminiature.interactions

sealed interface Action {
    data class OnAppear(val projectId: Long) : Action
    data class OnNameChanged(val name: String) : Action
    data object OnImageChanged : Action //TODO : Complete with Uri
    data object OnAddMiniature : Action
}