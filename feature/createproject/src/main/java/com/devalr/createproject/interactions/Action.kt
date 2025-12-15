package com.devalr.createproject.interactions

sealed interface Action {
    data object OnAppear : Action
}