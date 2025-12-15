package com.devalr.createproject.interactions

sealed interface Action {
    data object OnAppear : Action
    data class OnNameChanged(val name: String) : Action
    data class OnDescriptionChanged(val description: String) : Action
    data object OnImageChanged : Action //TODO : Complete with Uri
    data object OnAddProject : Action
}